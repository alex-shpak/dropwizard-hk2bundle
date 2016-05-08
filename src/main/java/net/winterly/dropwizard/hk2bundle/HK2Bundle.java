package net.winterly.dropwizard.hk2bundle;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.util.component.LifeCycle;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.servlet.ServletProperties;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HK2Bundle implements ConfiguredBundle<Configuration> {

    private final ServiceLocator serviceLocator;
    private final Application application;

    public HK2Bundle(Application application, AbstractBinder... binders) {
        this.application = application;
        this.serviceLocator = ServiceLocatorUtilities.bind(binders);

        populate(serviceLocator);

        serviceLocator.inject(application);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        ServiceLocatorUtilities.bind(serviceLocator, new AbstractBinder() {
            @Override
            protected void configure() {
                bind(application);
                bind(application).to(Application.class);

                bind(configuration);
                bind(environment);
                bind(environment.getObjectMapper());
            }
        });

        LifecycleEnvironment lifecycle = environment.lifecycle();
        AdminEnvironment admin = environment.admin();

        listServices(HealthCheck.class).forEach(healthCheck -> {
            String name = healthCheck.getClass().getSimpleName();
            environment.healthChecks().register(name, healthCheck);
        });

        listServices(Managed.class).forEach(lifecycle::manage);
        listServices(LifeCycle.class).forEach(lifecycle::manage);
        listServices(LifeCycle.Listener.class).forEach(lifecycle::addLifeCycleListener);
        listServices(ServerLifecycleListener.class).forEach(lifecycle::addServerLifecycleListener);
        listServices(Task.class).forEach(admin::addTask);

        //Set service locator as parent for Jersey's service locator
        environment.getApplicationContext().setAttribute(ServletProperties.SERVICE_LOCATOR, serviceLocator);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        listServices(Bundle.class).forEach(bootstrap::addBundle);
        listServices(Command.class).forEach(bootstrap::addCommand);
    }

    private <T> List<T> listServices(Class<T> type) {
        TypeFilter filter = new TypeFilter(type);
        return serviceLocator.getAllServices(filter).stream().map(type::cast).collect(Collectors.toList());
    }

    /**
     * Populate serviceLocator with services from classpath
     * @see <a href="https://hk2.java.net/2.4.0-b16/inhabitant-generator.html">https://hk2.java.net/2.4.0-b16/inhabitant-generator.html</a>
     */
    private static void populate(ServiceLocator serviceLocator) {
        DynamicConfigurationService dcs = serviceLocator.getService(DynamicConfigurationService.class);

        try {
            dcs.getPopulator().populate();
        }
        catch (IOException | MultiException e) {
            throw new MultiException(e);
        }
    }


}
