package net.winterly.dropwizard.hk2bundle;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.dropwizard.hk2bundle.validation.HK2ValidationBundle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.reflection.ReflectionHelper;
import org.glassfish.jersey.servlet.ServletProperties;

import java.util.List;

import static org.glassfish.hk2.utilities.ServiceLocatorUtilities.addOneConstant;

public class HK2Bundle implements Bundle {

    private final ServiceLocator serviceLocator;

    private Application application;

    HK2Bundle(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
        ServiceLocatorUtilities.bind(serviceLocator, new BundleBinder());
    }

    /**
     * Creates new instance of {@link HK2BundleBuilder}
     *
     * @return new builder
     */
    public static HK2BundleBuilder builder() {
        return new HK2BundleBuilder();
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
    public void run(Environment environment) {
        addOneConstant(serviceLocator, environment);
        addOneConstant(serviceLocator, environment.getObjectMapper());
        addOneConstant(serviceLocator, environment.metrics());
        addOneConstant(serviceLocator, environment.getValidator());

        LifecycleEnvironment lifecycle = environment.lifecycle();
        MetricRegistry metricRegistry = environment.metrics();
        AdminEnvironment admin = environment.admin();

        listServices(HealthCheck.class).forEach(healthCheck -> {
            environment.healthChecks().register(getName(healthCheck), healthCheck);
        });
        listServices(Metric.class).forEach(metric -> {
            metricRegistry.register(getName(metric), metric);
        });
        listServices(Managed.class).forEach(lifecycle::manage);
        listServices(LifeCycle.class).forEach(lifecycle::manage);
        listServices(LifeCycle.Listener.class).forEach(lifecycle::addLifeCycleListener);
        listServices(ServerLifecycleListener.class).forEach(lifecycle::addServerLifecycleListener);
        listServices(Task.class).forEach(admin::addTask);

        environment.jersey().register(HK2LifecycleListener.class);

        //Set service locator as parent for Jersey's service locator
        environment.getApplicationContext().setAttribute(ServletProperties.SERVICE_LOCATOR, serviceLocator);
        environment.getAdminContext().setAttribute(ServletProperties.SERVICE_LOCATOR, serviceLocator);

        serviceLocator.inject(application);
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

    private static class BundleBinder extends DropwizardBinder {

        @Override
        protected void configure() {
            register(HK2ValidationBundle.class);
            register(HK2ConfiguredBundle.class);
        }
    }
}
