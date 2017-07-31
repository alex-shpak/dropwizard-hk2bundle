package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;

public class DSFFactory implements Factory<DataSourceFactory> {

    @Inject
    private Configuration configuration;

    @Inject
    private DataSourceFactoryProvider<Configuration> dataSourceFactoryProvider;

    @Override
    public DataSourceFactory provide() {
        return dataSourceFactoryProvider.apply(configuration);
    }

    @Override
    public void dispose(DataSourceFactory instance) {

    }
}