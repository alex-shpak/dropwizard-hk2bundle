package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.setup.Bootstrap;
import net.winterly.dropwizard.hk2bundle.jdbi.JDBIBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.glassfish.hk2.utilities.ServiceLocatorUtilities.addOneConstant;

public class HK2BundleBuilder {

    private final List<Binder> binders = new ArrayList<>();
    private final List<Bundle> bundles = new ArrayList<>();

    HK2BundleBuilder() {

    }

    /**
     * Registers binders to be registered in DI container during build
     *
     * @param binders array of binders
     * @return self
     * @see DropwizardBinder
     */
    public HK2BundleBuilder bind(Binder... binders) {
        Collections.addAll(this.binders, binders);
        return this;
    }

    /**
     * Registers bundles to be registered in DI container during build
     *
     * @param bundles array of bundle instances
     * @return self
     */
    public HK2BundleBuilder bundle(Bundle... bundles) {
        Collections.addAll(this.bundles, bundles);
        return this;
    }

    /**
     * Add inject bindings for DBI and DAO objects
     *
     * @param databaseConfiguration lambda returning {@link io.dropwizard.db.DataSourceFactory} for JDBI
     * @param <T>                   configuration generic type
     * @return self
     */
    public <T extends Configuration> HK2BundleBuilder jdbi(DatabaseConfiguration<T> databaseConfiguration) {
        binders.add(new JDBIBinder(databaseConfiguration));
        return this;
    }

    /**
     * Same as {@link #jdbi(DatabaseConfiguration)} with convenience generic class
     *
     * @param databaseConfiguration lambda returning {@link io.dropwizard.db.DataSourceFactory} for JDBI
     * @param configurationClass    configuration instance for defining {@link T}
     * @param <T>                   configuration generic type
     * @return self
     */
    public <T extends Configuration> HK2BundleBuilder jdbi(DatabaseConfiguration<T> databaseConfiguration, Class<T> configurationClass) {
        jdbi(databaseConfiguration);
        return this;
    }

    /**
     * Creates and initializes {@link HK2Bundle} as well as bootstrap {@link ServiceLocator}
     *
     * @return created bundle instance
     */
    public HK2Bundle build() {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        ServiceLocatorUtilities.bind(serviceLocator, binders.toArray(new Binder[binders.size()]));

        bundles.forEach(bundle -> {
            serviceLocator.inject(bundle);
            addOneConstant(serviceLocator, bundle);
        });

        return new HK2Bundle(serviceLocator);
    }

    public void attach(Bootstrap bootstrap) {
        bootstrap.addBundle(build());
    }
}
