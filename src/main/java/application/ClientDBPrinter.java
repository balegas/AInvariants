package application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.IntStream;

import javax.management.MalformedObjectNameException;

import org.apache.commons.io.output.NullOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import application.configurations.ExecutorProperties;
import application.internal.io.CSVOutput;
import application.warehouse.repository.ItemsRepository;

@Component
@Scope("prototype")
@Profile("printer")
public class ClientDBPrinter implements Runnable {

    Logger logger = LoggerFactory.getLogger(ClientDBPrinter.class);

    private @Autowired ExecutorProperties config;
    private @Autowired ItemsRepository repository;
    private OutputStream os;

    public ClientDBPrinter() {
    }

    @Override
    public void run() {
        int nKeys = config.getnKeys();
        long[] data = new long[nKeys + 1];

        IntStream.range(0, nKeys).forEach(i -> {
            data[i] = repository.getStock(i);
        });

        StringBuilder log = new StringBuilder("END DB STATE: ");
        for (int i = 0; i <= nKeys; i++) {
            log.append(data[i]).append(",");
        }

        if (os != null) {
            CSVOutput.print(os, log);
        }

        logger.info(log.toString());
    }

    public void setResultsOS(OutputStream os) {
        this.os = os;
    }

    public static void main(String[] args) throws IOException, MalformedObjectNameException, InterruptedException {
        System.setProperty("spring.profiles.active", "printer");
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        ClientDBPrinter dbPrinterExecutor = context.getBean(ClientDBPrinter.class);
        ExecutorProperties config = context.getBean(ExecutorProperties.class);
        OutputStream os;
        if (!StringUtils.isEmpty(config.getOutputFile())) {
            os = new BufferedOutputStream(new FileOutputStream(new File(config.getOutputFile())));
        } else {
            os = new NullOutputStream();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown Hook is running !");
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        dbPrinterExecutor.setResultsOS(os);
        dbPrinterExecutor.run();

    }
}
