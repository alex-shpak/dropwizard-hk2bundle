package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.ContractsProvided;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.Set;

import static org.glassfish.hk2.utilities.ServiceLocatorUtilities.addOneConstant;
import static org.glassfish.hk2.utilities.reflection.ReflectionHelper.getAdvertisedTypesFromObject;

/**
 * Internal configured bundle used to obtain and bind configuration instance into DI container.
 */
@Singleton
@ContractsProvided(ConfiguredBundle.class)
class HK2ConfigurationBundle implements ConfiguredBundle<Configuration> {

    private final ServiceLocator serviceLocator;

    @Inject
    HK2ConfigurationBundle(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) { }

    @Override
    public void run(Configuration configuration, Environment environment) {
        Set<Type> contracts = getAdvertisedTypesFromObject(configuration, Contract.class);
        contracts.add(Configuration.class);

        addOneConstant(serviceLocator, configuration, null, contracts.toArray(new Type[0]));
    }
}
