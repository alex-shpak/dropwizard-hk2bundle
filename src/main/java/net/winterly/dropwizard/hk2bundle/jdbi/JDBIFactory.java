package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.Factory;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import javax.inject.Named;

public class JDBIFactory implements Factory<DBI> {

    private final DBIFactory factory = new DBIFactory();
    private final String name = getClass().getSimpleName();

    @Inject
    private Environment environment;

    @Inject
    private Configuration configuration;

    @Inject
    @Named("jdbi")
    private DatabaseConfiguration<Configuration> databaseConfiguration;

    @Override
    public DBI provide() {
        return factory.build(
                environment,
                databaseConfiguration.getDataSourceFactory(configuration),
                name
        );
    }

    @Override
    public void dispose(DBI instance) {

    }
}
