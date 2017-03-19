package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.Factory;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;

public class JDBIFactory implements Factory<DBI> {

    @Inject
    private Environment environment;

    @Inject
    private DataSourceFactory dataSourceFactory;

    private final DBIFactory factory = new DBIFactory();
    private final String name = getClass().getSimpleName();

    @Override
    public DBI provide() {
        return factory.build(environment, dataSourceFactory, name);
    }

    @Override
    public void dispose(DBI instance) {

    }
}
