package application;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import javax.management.MalformedObjectNameException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import application.configurations.ExecutorProperties;

@SpringBootApplication
public class Main {

	// CREATE KEYSPACE IF NOT EXISTS warehouse WITH replication =
	// {'class':'SimpleStrategy','replication_factor':'1'};

	public static void main(String[] args) throws IOException, MalformedObjectNameException, InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(Main.class);
		ExecutorProperties config = context.getBean(ExecutorProperties.class);
		int nThreads = config.getnThreads();
		Random rand = new Random();

		if (args.length > 0 && (args[0].equals("--init") || args[0].equals("-i"))) {
			Initializer initializer = context.getBean(Initializer.class);
			initializer.init();
		} else {
			CountDownLatch latch = new CountDownLatch(nThreads);

			IntStream.range(0, nThreads).parallel().forEach(i -> {
				Client executor = context.getBean(Client.class);
				executor.setId(i);
				executor.setRand(rand);
				executor.setLatch(latch);
				executor.run();
			});

			latch.await();
		}
		
		System.exit(0);
	}
}