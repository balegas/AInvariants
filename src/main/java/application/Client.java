package application;

import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
        int percentageRO = config.getPercentageRO();

        int[] data = new int[nKeys + 1];
        int[] counts = new int[nKeys + 1];

        IntStream.range(0, nOps).forEach(i -> {
            thinkTime();
            boolean ro = Math.random() * 100 < percentageRO;
            int key = Math.toIntExact((long) keyGenerator.nextValue());
            if (ro) {
                Long value = repository.getStock(key);
                if (os != null) {
                    CSVOutput.print(os, System.currentTimeMillis(), id, "RO", key, value, value);
                }

            } else {
                boolean dec = Math.random() * 100 < percentageDecs;

                int amount = Math.toIntExact((long) valueGenerator.nextValue());
                data[key]++;
                Long value = repository.getStock(key);

                if (value <= 0) {
                    repository.setValue(key, config.getInitValMin());
                    if (os != null) {
                        CSVOutput.print(os, System.currentTimeMillis(), id, "SET", key, amount, value);
                    }
                } else if (dec) {
                    repository.decrement(key, Math.min(value, amount));
                    counts[key] -= amount;
                } else {
                    repository.increment(key, amount);
                    counts[key] += amount;
                }

                if (os != null) {
                    CSVOutput.print(os, System.currentTimeMillis(), id, dec ? "DEC" : "INC", key, amount, value);
                }
            }
        });

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

}
