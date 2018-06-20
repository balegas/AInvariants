package application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.ImmutableList;

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
        List<Map<String, Object>> columns = repository.getAllStock();
        CSVOutput.printColumnValues(os, columns, "id");
        while (true) {
            List<Map<String, Object>> stocks = repository.getAllStock();
            if (os != null) {
                CSVOutput.printListMap(os, stocks, (List<String>) ImmutableList.of("id", "stock"));
            }
            thinkTime();
            logger.info(stocks.toString());
        }
    }

    public void setResultsOS(OutputStream os) {
        this.os = os;
    }

    private void thinkTime() {
        int sleepTime = config.getPrintIntervalMS();
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dbPrinterExecutor.setResultsOS(os);
        dbPrinterExecutor.run();

    }
}
