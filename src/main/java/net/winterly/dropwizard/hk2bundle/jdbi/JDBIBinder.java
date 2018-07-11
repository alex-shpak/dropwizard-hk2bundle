package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DatabaseConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashSet;

import static java.util.Objects.requireNonNull;

public class JDBIBinder<T extends Configuration> extends AbstractBinder {

    private Class<? extends Factory<DBI>> dbiFactory = JDBIFactory.class;
    private Class<? extends Factory<Object>> sqlObjectFactory = SqlObjectFactory.class;

    private DatabaseConfiguration<T> databaseConfiguration;
    private HashSet<Class<?>> sqlInterfaces = new HashSet<>();

    public JDBIBinder<T> setDatabaseConfiguration(DatabaseConfiguration<T> databaseConfiguration) {
        this.databaseConfiguration = requireNonNull(databaseConfiguration);
        return this;
    }

    public JDBIBinder<T> setDBIFactory(Class<? extends Factory<DBI>> dbiFactory) {
        this.dbiFactory = requireNonNull(dbiFactory);
        return this;
    }

    public JDBIBinder<T> setSqlObjectFactory(Class<? extends Factory<Object>> sqlObjectFactory) {
        this.sqlObjectFactory = requireNonNull(sqlObjectFactory);
        return this;
    }

    public JDBIBinder<T> addSqlObjects(Class<?>... interfaces) {
        Collections.addAll(sqlInterfaces, interfaces);
        return this;
    }

    @Override
    protected void configure() {
        bind(databaseConfiguration).to(DatabaseConfiguration.class);
        bindFactory(dbiFactory).to(DBI.class).in(Singleton.class);

        ServiceBindingBuilder<?> binding = bindFactory(sqlObjectFactory);
        sqlInterfaces.forEach(binding::to);
        binding.in(Singleton.class);
    }
}
