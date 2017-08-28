package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import java.util.function.Function;

/**
 * Converts {@link Configuration} into {@link DataSourceFactory} for configuring database
 */
public interface DataSourceFactoryProvider extends Function<Configuration, DataSourceFactory> {
}
