package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.ServiceLocator;

import static org.glassfish.hk2.utilities.ServiceLocatorUtilities.addOneConstant;

class HK2ConfiguredBundle implements ConfiguredBundle<Configuration> {

    private final ServiceLocator serviceLocator;

    HK2ConfiguredBundle(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        addOneConstant(serviceLocator, configuration);
    }

}
