package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

		if (args.length > 0 && (args[0].equals("--init") || args[0].equals("-i"))) {
			Initializer initializer = context.getBean(Initializer.class);
			initializer.init();
		} else {
			ExecutorProperties config = context.getBean(ExecutorProperties.class);
			OutputStream os = new FileOutputStream(new File("client-results.out"));
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
		}

		System.exit(0);
	}
}