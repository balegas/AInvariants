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

    private Cluster cluster;

    @Autowired
    public ApplicationConfiguration(Cluster cluster) {
        this.cluster = cluster;
    }

    public @Bean CqlTemplate cqlTemplate() {
        Session session = cluster.connect(config.getKeyspace());
        CqlTemplate template = new CqlTemplate(session);
        template.setConsistencyLevel(config.getConsistency());
        return template;
    }

    @Bean
    public Generator<?> keyGenerator() {
        String className = config.getKeyGeneratorClass() != null ? config.getKeyGeneratorClass() : "";

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
        String className = config.getValueGeneratorClass() != null ? config.getValueGeneratorClass() : "";

        switch (className) {
        case "HotspotIntegerGenerator":
            return new HotspotIntegerGenerator(0, config.getDeltaRange(), Double.parseDouble(config.getValueDistArg0()),
                    Double.parseDouble(config.getValueDistArg1()));
        default:
            return new UniformLongGenerator(0, config.getnKeys());
        }
    }

    @Bean
    public Generator<?> sleepTimeGenerator() {
        String className = config.getSleepGeneratorClass() != null ? config.getSleepGeneratorClass() : "";
        switch (className) {
        case "HotspotIntegerGenerator":
            return new HotspotIntegerGenerator(config.getMinSleepTime(), config.getMaxSleepTime(),
                    Double.parseDouble(config.getSleepDistArg0()), Double.parseDouble(config.getSleepDistArg1()));
        default:
            return new UniformLongGenerator(0, config.getnKeys());
        }
    }

    @Bean
    public Generator<?> clientGenerator() {
        String className = config.getClientsGeneratorClass() != null ? config.getClientsGeneratorClass() : "";
        switch (className) {
        case "HotspotIntegerGenerator":
            return new HotspotIntegerGenerator(0, config.getnThreads(), Double.parseDouble(config.getClientDistArg0()),
                    Double.parseDouble(config.getClientDistArg1()));
        default:
            return new UniformLongGenerator(0, config.getnKeys());
        }
    }
}
