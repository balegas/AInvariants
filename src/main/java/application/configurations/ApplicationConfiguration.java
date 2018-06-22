package application.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import application.generator.Generator;
import application.generator.HotspotIntegerGenerator;
import application.generator.UniformLongGenerator;

@Configuration
@EnableConfigurationProperties(ExecutorProperties.class)
public class ApplicationConfiguration {

    @Autowired
    ExecutorProperties config;

    public @Bean Session session() {
        Cluster cluster = Cluster.builder().addContactPoints("localhost").build();
        return cluster.connect(config.getKeyspace());
    }

    public @Bean CqlTemplate cqlTemplate() {
        return new CqlTemplate(session());
    }

    @Bean
    public Generator<?> keyGenerator() {
        String className = config.getKeyGeneratorClass();
        if (className == null) {
            className = "";
        }

        switch (className) {
        case "HotspotIntegerGenerator":
            return new HotspotIntegerGenerator(0, config.getnKeys() - 1, Double.parseDouble(config.getKeyDistArg0()),
                    Double.parseDouble(config.getKeyDistArg1()));
        default:
            return new UniformLongGenerator(0, config.getnKeys());
        }
    }

    @Bean
    public Generator<?> valueGenerator() {
        String className = config.getKeyGeneratorClass();
        if (className == null) {
            className = "";
        }

        switch (className) {
        case "HotspotIntegerGenerator":
            return new HotspotIntegerGenerator(0, config.getDeltaRange(), Double.parseDouble(config.getKeyDistArg0()),
                    Double.parseDouble(config.getKeyDistArg1()));
        default:
            return new UniformLongGenerator(0, config.getnKeys());
        }
    }
}
