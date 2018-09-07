package net.winterly.dropwizard.hk2bundle;

import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import static org.glassfish.hk2.extras.ExtrasUtilities.bridgeServiceLocator;

public class HK2BridgeFeature implements Feature {

    @Inject
    private ServiceLocator jerseyServiceLocator;

    private ServiceLocator bundleServiceLocator;

    public HK2BridgeFeature(ServiceLocator serviceLocator) {
        this.bundleServiceLocator = serviceLocator;
    }

    @Override
    public boolean configure(FeatureContext context) {
        bridgeServiceLocator(bundleServiceLocator, jerseyServiceLocator);
        bridgeServiceLocator(jerseyServiceLocator, bundleServiceLocator);

        return true;
    }
}
