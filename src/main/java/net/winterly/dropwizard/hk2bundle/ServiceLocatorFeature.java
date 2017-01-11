package net.winterly.dropwizard.hk2bundle;

import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Re-injects all services with new jersey service locator when it is ready
 */
public class ServiceLocatorFeature implements Feature {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public boolean configure(FeatureContext context) {
        serviceLocator.getParent()
                .getAllServiceHandles(d -> true)
                .stream()
                .filter(ServiceHandle::isActive)
                .map(ServiceHandle::getService)
                .forEach(service -> serviceLocator.inject(service));

        return true;
    }
}
