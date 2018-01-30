package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;

import static org.glassfish.hk2.utilities.ServiceLocatorUtilities.addOneConstant;

/**
 * Configured bundle used to obtain and bind configuration instance into DI container.
 */
class HK2ConfiguredBundle implements ConfiguredBundle<Configuration> {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        addOneConstant(serviceLocator, configuration);
    }
}
