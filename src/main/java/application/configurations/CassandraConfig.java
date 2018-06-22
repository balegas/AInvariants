package application.configurations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.util.StringUtils;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    private @Autowired ExecutorProperties config;
    // private static final String USERNAME = "cassandra";
    // private static final String PASSWORD = "cassandra";
    // private static final String NODES = "127.0.0.1"; // comma seperated nodes

    @Bean
    @Override
    public CassandraCqlClusterFactoryBean cluster() {
        CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
        bean.setKeyspaceCreations(getKeyspaceCreations());
        bean.setContactPoints(StringUtils.arrayToCommaDelimitedString(config.getCassandraEndpoints()));
        // bean.setUsername(USERNAME);
        // bean.setPassword(PASSWORD);
        return bean;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getKeyspaceName() {
        return config.getKeyspace();
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { "applicaytion.model" };
    }

    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        List<CreateKeyspaceSpecification> createKeyspaceSpecifications = new ArrayList<>();
        createKeyspaceSpecifications.add(getKeySpaceSpecification());
        return createKeyspaceSpecifications;
    }

    // Below method creates "my_keyspace" if it doesnt exist.
    @SuppressWarnings("static-access")
    private CreateKeyspaceSpecification getKeySpaceSpecification() {
        CreateKeyspaceSpecification warehouseKeyspace = CreateKeyspaceSpecification
                .createKeyspace(config.getKeyspace());
        warehouseKeyspace.ifNotExists(true).createKeyspace(config.getKeyspace());
        return warehouseKeyspace;
    }

}