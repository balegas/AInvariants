package application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import javax.management.MalformedObjectNameException;

import org.apache.commons.io.output.NullOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import application.configurations.ExecutorProperties;
import application.generator.Generator;
import application.internal.io.CSVOutput;
import application.warehouse.repository.ItemsRepository;

@Component
@Scope("prototype")
@Profile("client")
public class Client implements Runnable {

    Logger logger = LoggerFactory.getLogger(Client.class);

    private @Autowired ExecutorProperties config;
    private @Autowired ItemsRepository repository;
    private @Autowired @Qualifier("keyGenerator") Generator<?> keyGenerator;
    private @Autowired @Qualifier("valueGenerator") Generator<?> valueGenerator;
    private int id;
    private Random rand;

    private CountDownLatch latch;

    private OutputStream os;

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    @Override
    public void run() {
        int nOps = config.getnOps();
        int nKeys = config.getnKeys();
        int percentageDecs = config.getPercentageDecs();

        int[] data = new int[nKeys + 1];
        int[] counts = new int[nKeys + 1];

        IntStream.range(0, nOps).forEach(i -> {
            thinkTime();
            boolean dec = Math.random() * 100 < percentageDecs;
            int key = Math.toIntExact((long) keyGenerator.nextValue());
            int amount = Math.toIntExact((long) valueGenerator.nextValue());
            data[key]++;
            if (dec) {
                // logger.info(String.format("Executor %d: decrement %d. TOTOAL OPS: %d", id, amount, i));
                repository.decrement(key, amount);
                counts[key] -= amount;
            } else {
                // logger.info(String.format("Executor %d: increment %d. TOTOAL OPS: %d", id, amount, i));
                repository.increment(key, amount);
                counts[key] += amount;
            }

            // Timestamp, clientId, optype, key, value
            if (os != null) {
                CSVOutput.print(os, System.currentTimeMillis(), i, dec ? "DEC" : "INC", key, amount);
            }

        });

        /*
         * for (int j = 0; j <= nKeys; j++) { System.out.format("%d, ", data[j]); } System.out.format("\n");
         */
        for (int j = 0; j <= nKeys; j++) {
            System.out.format("%d, ", counts[j]);
        }
        System.out.format("\n");

        latch.countDown();
    }

    private void thinkTime() {
        int minLatency = config.getSleepTimeMinMs();
        int maxLatency = config.getSleepTimeMaxMs();

        try {
            Thread.sleep(minLatency + rand.nextInt(maxLatency - minLatency));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public void setResultsOS(OutputStream os) {
        this.os = os;
    }

    public static void main(String[] args) throws IOException, MalformedObjectNameException, InterruptedException {
        System.setProperty("spring.profiles.active", "client");
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        ExecutorProperties config = context.getBean(ExecutorProperties.class);
        OutputStream os;
        if (!StringUtils.isEmpty(config.getOutputFile())) {
            os = new BufferedOutputStream(new FileOutputStream(new File(config.getOutputFile())));
        } else {
            os = new NullOutputStream();
        }

        Random rand = new Random();
        int nThreads = config.getnThreads();
        CountDownLatch latch = new CountDownLatch(nThreads);

        IntStream.range(0, nThreads).parallel().forEach(i -> {
            Client executor = context.getBean(Client.class);
            executor.setId(i);
            executor.setRand(rand);
            executor.setLatch(latch);
            executor.setResultsOS(os);
            executor.run();
        });

        latch.await();
        os.close();

        System.exit(0);
    }

}
