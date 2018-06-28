package application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
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
import application.generator.Generator;
import application.internal.io.CSVOutput;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && (args[0].equals("--init") || args[0].equals("-i"))) {
            System.out.println("Running Initializer");
            startInitializer();
        } else if (args.length > 0 && (args[0].equals("--print") || args[0].equals("-o"))) {
            System.out.println("Running Printer");
            startPrinter();
        } else {
            System.out.println("Running Client");
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

        Map<Integer, AtomicBoolean> clientActivity = new HashMap<>();

        IntStream.range(0, nThreads).parallel().forEach(i -> {
            System.out.println("Starting client " + i);
            Client executor = context.getBean(Client.class);
            AtomicBoolean active = new AtomicBoolean(true);
            executor.setId(i);
            executor.setRand(rand);
            executor.setLatch(latch);
            executor.setResultsOS(os);
            executor.setActive(active);
            clientActivity.put(i, active);
            new Thread(executor).start();
        });

        if (config.getClientAdjustInterval() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Random r = new Random();
                    Generator generator = context.getBean("clientGenerator", Generator.class);
                    while (true) {
                        try {
                            Thread.sleep(config.getClientAdjustInterval());
                            long n = (long) generator.nextValue();
                            if (n == 0) {
                                continue;
                            }
                            IntStream ints = r.ints(n, 0, config.getnThreads());
                            ints.forEach(i -> {
                                AtomicBoolean active = clientActivity.get(i);
                                if (active.get()) {
                                    active.set(false);
                                } else {
                                    active.set(true);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        latch.await();
        os.close();
        System.out.println("All clients finished");
        System.exit(0);

    }
}