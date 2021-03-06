package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DatabaseConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashSet;

import static java.util.Objects.requireNonNull;

/**
 * HK2 binder configuring and binding SQL Objects injection
 *
 * @param <T> application configuration type that contains {@link io.dropwizard.db.PooledDataSourceFactory}
 */
public class JDBIBinder<T extends Configuration> extends AbstractBinder {

    private Class<? extends Factory<Jdbi>> dbiFactory = JDBIFactory.class;
    private Class<? extends Factory<Object>> sqlObjectFactory = SqlObjectFactory.class;

    private DatabaseConfiguration<T> databaseConfiguration;
    private HashSet<Class<?>> sqlInterfaces = new HashSet<>();

    /**
     * @param databaseConfiguration database configuration provider
     */
    public JDBIBinder(DatabaseConfiguration<T> databaseConfiguration) {
        this.databaseConfiguration = requireNonNull(databaseConfiguration);
    }

    /**
     * Set factory that creates DBI instance
     *
     * @param dbiFactory dbi factory type
     * @return self
     * @see JDBIFactory
     */
    public JDBIBinder<T> setDBIFactory(Class<? extends Factory<Jdbi>> dbiFactory) {
        this.dbiFactory = requireNonNull(dbiFactory);
        return this;
    }

    /**
     * Set SQL object proxy factory
     *
     * @param sqlObjectFactory proxy factory type
     * @return self
     * @see SqlObjectFactory
     */
    public JDBIBinder<T> setSqlObjectFactory(Class<? extends Factory<Object>> sqlObjectFactory) {
        this.sqlObjectFactory = requireNonNull(sqlObjectFactory);
        return this;
    }

    /**
     * Register list of sql object interfaces
     *
     * @param interfaces interfaces
     * @return self
     */
    public JDBIBinder<T> register(Class<?>... interfaces) {
        Collections.addAll(sqlInterfaces, interfaces);
        return this;
    }

    @Override
    protected void configure() {
        bind(databaseConfiguration).to(new TypeLiteral<DatabaseConfiguration<Configuration>>() {});

        addActiveFactoryDescriptor(dbiFactory);
        addActiveFactoryDescriptor(HandleFactory.class);

        sqlInterfaces.forEach(type -> bindFactory(sqlObjectFactory)
                .to(type) //this will bind to Factory<Type>
                .in(Singleton.class)
        );
    }
}
