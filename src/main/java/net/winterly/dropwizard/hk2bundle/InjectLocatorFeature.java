package net.winterly.dropwizard.hk2bundle;

import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Re-injects all services with jersey service locator when it is ready.
 * <p>
 * In case if services that was created before jersey initialization need to access services from child service locator
 * <p>
 * Use wisely, might cause big performance hit on startup
 */
public class InjectLocatorFeature implements Feature {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public boolean configure(FeatureContext context) {
        serviceLocator.getParent()
                .getAllServiceHandles(service -> true)
                .stream()
                .filter(ServiceHandle::isActive)
                .map(ServiceHandle::getService)
                .forEach(service -> serviceLocator.inject(service));

        return true;
    }
}
