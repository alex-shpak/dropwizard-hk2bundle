package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.setup.Bootstrap;
import net.winterly.dropwizard.hk2bundle.jdbi.DataSourceFactoryProvider;
import net.winterly.dropwizard.hk2bundle.jdbi.JDBIBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HK2BundleBuilder {

    private final List<Binder> binders = new ArrayList<>();

    HK2BundleBuilder() {
    }

    public HK2BundleBuilder bind(Binder... binders) {
        Collections.addAll(this.binders, binders);
        return this;
    }

    public HK2BundleBuilder withJDBI(DataSourceFactoryProvider dataSourceFactoryProvider) {
        binders.add(new JDBIBinder(dataSourceFactoryProvider));
        return this;
    }

    public HK2Bundle build() {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        ServiceLocatorUtilities.bind(serviceLocator, binders.toArray(new Binder[]{}));

        return new HK2Bundle(serviceLocator);
    }

    public void attach(Bootstrap bootstrap) {
        bootstrap.addBundle(build());
    }
}
