package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.Factory;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JDBIFactory implements Factory<DBI> {

    private final DBIFactory factory = new DBIFactory();
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
    public DBI provide() {
        PooledDataSourceFactory dataSourceFactory = databaseConfiguration.getDataSourceFactory(configuration);
        return factory.build(environment, dataSourceFactory, name);
    }

    @Override
    public void dispose(DBI instance) {

    }
}
