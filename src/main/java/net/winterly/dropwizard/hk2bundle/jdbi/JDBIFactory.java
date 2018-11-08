package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.Factory;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * HK2 Factory for {@link Jdbi} instance, uses dropwizard configuration and environment to create instance
 */
@Singleton
public class JDBIFactory implements Factory<Jdbi> {

    private final JdbiFactory factory = new JdbiFactory();
    private final String name = getClass().getSimpleName();

    private final Environment environment;
    private final Configuration configuration;
    private final DatabaseConfiguration<Configuration> databaseConfiguration;

    @Inject
    public JDBIFactory(Environment environment, Configuration configuration,
                       DatabaseConfiguration<Configuration> databaseConfiguration) {
        this.environment = environment;
        this.configuration = configuration;
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    @Singleton
    public Jdbi provide() {
        PooledDataSourceFactory dataSourceFactory = databaseConfiguration.getDataSourceFactory(configuration);
        return factory.build(environment, dataSourceFactory, name);
    }

    @Override
    public void dispose(Jdbi instance) {

    }
}
