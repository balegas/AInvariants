package application;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import application.configurations.ExecutorProperties;
import application.generator.Generator;
import application.warehouse.repository.ItemsRepository;

@Component
@Scope("prototype")
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
            String[] output = new String[] { System.currentTimeMillis() + "", i + "", dec ? "DEC" : "INC", key + "",
                    amount + "" };
            if (os != null) {
                byte[] osb = StringUtils.arrayToCommaDelimitedString(output).getBytes();
                try {
                    synchronized (os) {
                        os.write(osb);
                        os.write('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

}
