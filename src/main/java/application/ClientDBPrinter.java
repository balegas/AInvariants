package application;

import application.configurations.ExecutorProperties;
import application.warehouse.repository.ItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@Component
@Scope("prototype")
public class ClientDBPrinter implements Runnable {

    Logger logger = LoggerFactory.getLogger(ClientDBPrinter.class);

    private @Autowired
    ExecutorProperties config;
    private @Autowired
    ItemsRepository repository;

    private CountDownLatch latch;


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

        logger.info(log.toString());
        latch.countDown();
    }

    void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}
