package application;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import application.configurations.ExecutorProperties;
import application.warehouse.repository.ItemsRepository;

@Component
@Scope("prototype")
public class Client implements Runnable {

	Logger logger = LoggerFactory.getLogger(Client.class);

	private @Autowired ExecutorProperties config;
	private @Autowired ItemsRepository repository;
	private int id;
	private Random rand;

	private CountDownLatch latch;

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
		int deltaRange = config.getDeltaRange();

		IntStream.range(0, nOps).forEach(i -> {
			thinkTime();
			boolean dec = Math.random() * 100 < percentageDecs ? true : false;
			int key = rand.nextInt(nKeys);
			int amount = rand.nextInt(deltaRange);
			if (dec) {
				logger.info(String.format("Executor %d: decrement %d. TOTOAL OPS: %d", id, amount, i));
				repository.decrement(key, amount);
			} else {
				logger.info(String.format("Executor %d: increment %d. TOTOAL OPS: %d", id, amount, i));
				repository.increment(key, amount);
			}
		});

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

}
