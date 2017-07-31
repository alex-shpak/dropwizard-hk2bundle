package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.Factory;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import javax.inject.Provider;

public class JDBIFactory implements Factory<DBI> {

    private final DBIFactory factory = new DBIFactory();
    private final String name = getClass().getSimpleName();

    @Inject
    private Provider<Environment> environment;

    @Inject
    private Provider<DataSourceFactory> dataSourceFactory;

    @Override
    public DBI provide() {
        return factory.build(environment.get(), dataSourceFactory.get(), name);
    }

    @Override
    public void dispose(DBI instance) {

    }
}
