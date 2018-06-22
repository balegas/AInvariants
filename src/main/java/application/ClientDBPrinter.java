package application;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
        CSVOutput.printColumnValues(os, columns, "id", true);
        while (true) {
            List<Map<String, Object>> stocks = repository.getAllStock();
            if (os != null) {
                CSVOutput.printListMap(os, stocks, (List<String>) ImmutableList.of("stock"), true);
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
}
