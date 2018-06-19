package application.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@Configuration
@EnableConfigurationProperties(ExecutorProperties.class)
public class ApplicationConfiguration {
	
	@Autowired ExecutorProperties config;

	public @Bean Session session() {
		Cluster cluster = Cluster.builder().addContactPoints("localhost").build();
		return cluster.connect(config.getKeyspace());
	}
	
	public @Bean CqlTemplate cqlTemplate() {
		return new CqlTemplate(session());
	}
}
