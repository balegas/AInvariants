package application;

import java.util.Random;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import application.configurations.ExecutorProperties;
import application.warehouse.repository.ItemsRepository;

@Component
public class Initializer {

    Logger logger = LoggerFactory.getLogger(Client.class);

    private @Autowired ExecutorProperties config;
    private @Autowired ItemsRepository repository;

    public void init() {
        int nKeys = config.getnKeys();
        int minVal = config.getInitValMin();
        int maxVal = config.getInitValMax();

        Random rand = new Random();

        repository.createItemsTable(true);

        IntStream.range(0, nKeys).parallel().forEach(i -> {
            int initVal = minVal + rand.nextInt(maxVal - minVal);
            logger.info(String.format("Adding item %d with value %d", i, initVal));
            repository.addItem(i, "item_" + i, initVal);
        });
    }
}
