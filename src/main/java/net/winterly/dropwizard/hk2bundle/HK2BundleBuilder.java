package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import javax.ws.rs.core.Feature;
import java.util.*;

public class HK2BundleBuilder {

    private final Application application;
    private final Set<Class<? extends Feature>> features = new HashSet<>();
    private final List<Binder> binders = new ArrayList<>();

    HK2BundleBuilder(Application application) {
        this.application = application;
    }

    public HK2BundleBuilder bind(Binder... binders) {
        Collections.addAll(this.binders, binders);
        return this;
    }

    public HK2BundleBuilder reinjectServices() {
        features.add(InjectLocatorFeature.class);
        return this;
    }

    public HK2Bundle build() {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        binders.forEach(binder -> ServiceLocatorUtilities.bind(serviceLocator, binder));

        return new HK2Bundle(application, serviceLocator, features);
    }

    public void attach(Bootstrap bootstrap) {
        bootstrap.addBundle(build());
    }
}
