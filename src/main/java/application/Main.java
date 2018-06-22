package application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.apache.commons.io.output.NullOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;

import application.configurations.ExecutorProperties;
import application.internal.io.CSVOutput;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && (args[0].equals("--init") || args[0].equals("-i"))) {
            startInitializer();
        }
        if (args.length > 0 && (args[0].equals("--print") || args[0].equals("-o"))) {
            startPrinter();
        } else {
            startClient();
        }
    }

    private static void startPrinter() throws FileNotFoundException {
        System.setProperty("spring.profiles.active", "printer");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        ExecutorProperties config = context.getBean(ExecutorProperties.class);
        System.out.println(config);

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

        ClientDBPrinter dbPrinterExecutor = context.getBean(ClientDBPrinter.class);
        dbPrinterExecutor.setResultsOS(os);
        dbPrinterExecutor.run();
    }

    Logger logger = LoggerFactory.getLogger(Client.class);

    private static void startInitializer() {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        ExecutorProperties config = context.getBean(ExecutorProperties.class);
        System.out.println(config);

        Initializer initializer = context.getBean(Initializer.class);
        initializer.init();
        System.exit(0);
    }

    private static void startClient() throws FileNotFoundException, Exception {
        System.setProperty("spring.profiles.active", "client");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        ExecutorProperties config = context.getBean(ExecutorProperties.class);
        System.out.println(config);

        OutputStream os;
        if (!StringUtils.isEmpty(config.getOutputFile())) {
            os = new BufferedOutputStream(new FileOutputStream(new File(config.getOutputFile())));
        } else {
            os = new NullOutputStream();
        }

        Random rand = new Random();
        int nThreads = config.getnThreads();
        CountDownLatch latch = new CountDownLatch(nThreads);

        if (os != null) {
            CSVOutput.printColumnNames(os, ImmutableList.of("TS", "clientId", "opType", "key", "value", "readValue"));
        }

        IntStream.range(0, nThreads).parallel().forEach(i -> {
            Client executor = context.getBean(Client.class);
            executor.setId(i);
            executor.setRand(rand);
            executor.setLatch(latch);
            executor.setResultsOS(os);
            new Thread(executor).start();
        });

        latch.await();
        os.close();
        System.out.println("All clients finished");
        System.exit(0);

    }
}