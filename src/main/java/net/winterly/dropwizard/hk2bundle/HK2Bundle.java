package net.winterly.dropwizard.hk2bundle;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
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
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.reflection.ReflectionHelper;

import java.util.List;

import static org.glassfish.hk2.utilities.ServiceLocatorUtilities.*;

public class HK2Bundle implements ConfiguredBundle<Configuration> {

    /**
     * Used to generate unique incremental IDs for ServiceLocator name
     */
    private static int nameCounter = 0;

    private final ServiceLocator serviceLocator;
    private Application application;

    public HK2Bundle(Binder... binders) {
        this(defaultServiceLocator(), binders);
    }

    public HK2Bundle(ServiceLocator serviceLocator, Binder... binders) {
        this.serviceLocator = serviceLocator;

        addClasses(serviceLocator, HK2ConfigurationBundle.class);
        bind(serviceLocator, binders);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        this.application = bootstrap.getApplication();

        // Register application ASAP, so other bundles can inject it
        addOneConstant(serviceLocator, application, null, Application.class, application.getClass());
        serviceLocator.inject(application);

        listServices(Bundle.class).forEach(bootstrap::addBundle);
        listServices(ConfiguredBundle.class).forEach(bootstrap::addBundle);
        listServices(Command.class).forEach(bootstrap::addCommand);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        addOneConstant(serviceLocator, environment);
        addOneConstant(serviceLocator, environment.jersey());
        addOneConstant(serviceLocator, environment.admin());
        addOneConstant(serviceLocator, environment.lifecycle());
        addOneConstant(serviceLocator, environment.servlets());
        addOneConstant(serviceLocator, environment.getObjectMapper());
        addOneConstant(serviceLocator, environment.getValidator());
        addOneConstant(serviceLocator, environment.metrics());
        addOneConstant(serviceLocator, environment.healthChecks());


        LifecycleEnvironment lifecycle = environment.lifecycle();
        MetricRegistry metricRegistry = environment.metrics();
        HealthCheckRegistry healthCheckRegistry = environment.healthChecks();
        AdminEnvironment admin = environment.admin();

        listServices(HealthCheck.class).forEach(healthCheck ->
                healthCheckRegistry.register(getName(healthCheck), healthCheck)
        );
        listServices(Metric.class).forEach(metric ->
                metricRegistry.register(getName(metric), metric)
        );
        listServices(MetricSet.class).forEach(metricRegistry::registerAll);
        listServices(Managed.class).forEach(lifecycle::manage);
        listServices(LifeCycle.class).forEach(lifecycle::manage);
        listServices(LifeCycle.Listener.class).forEach(lifecycle::addLifeCycleListener);
        listServices(ServerLifecycleListener.class).forEach(lifecycle::addServerLifecycleListener);
        listServices(Task.class).forEach(admin::addTask);

        // Re-inject application with registered objects
        serviceLocator.inject(application);

        environment.jersey().register(new HK2BridgeFeature(serviceLocator));
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    private <T> List<T> listServices(Class<T> type) {
        return serviceLocator.getAllServices(type);
    }

    private String getName(Object object) {
        String name = ReflectionHelper.getName(object.getClass());
        if (name == null) {
            return object.getClass().getSimpleName();
        }
        return name;
    }

    private synchronized static ServiceLocator defaultServiceLocator() {
        String locatorName = String.format("%s-%s",
                HK2Bundle.class.getSimpleName(),
                nameCounter++
        );
        return createAndPopulateServiceLocator(locatorName);
    }
}
