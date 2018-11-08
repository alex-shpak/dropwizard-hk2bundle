package net.winterly.dropwizard.hk2bundle.validation;

import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.util.function.Consumer;

/**
 * Utility jersey feature to get access to ServiceLocator
 */
public class GetLocatorFeature implements Feature {

    private final Consumer<ServiceLocator> action;

    @Inject
    private ServiceLocator serviceLocator;

    public GetLocatorFeature(Consumer<ServiceLocator> action) {
        this.action = action;
    }

    @Override
    public boolean configure(FeatureContext context) {
        action.accept(serviceLocator);
        return true;
    }
}
