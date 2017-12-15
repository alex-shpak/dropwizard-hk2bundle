package net.winterly.dropwizard.hk2bundle;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.cli.Command;
import io.dropwizard.jersey.setup.JerseyEnvironment;
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
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.ws.rs.core.Feature;
import java.util.List;
import java.util.Set;

public class HK2Bundle implements Bundle {

    private final ServiceLocator serviceLocator;
    private final Set<Class<? extends Feature>> features;

    private Application application;

    HK2Bundle(ServiceLocator serviceLocator, Set<Class<? extends Feature>> features) {
        this.serviceLocator = serviceLocator;
        this.features = features;
    }

    public static HK2BundleBuilder builder() {
        return new HK2BundleBuilder();
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        this.application = bootstrap.getApplication();

        bootstrap.addBundle(new HK2ConfiguredBundle(serviceLocator));
        bootstrap.addBundle(new HK2ValidationBundle(serviceLocator));

        listServices(Bundle.class).forEach(bootstrap::addBundle);
        listServices(ConfiguredBundle.class).forEach(bootstrap::addBundle);
        listServices(Command.class).forEach(bootstrap::addCommand);
    }

    @Override
    public void run(Environment environment) {
        ServiceLocatorUtilities.bind(serviceLocator, new EnvBinder(application, environment));

        JerseyEnvironment jersey = environment.jersey();
        LifecycleEnvironment lifecycle = environment.lifecycle();
        AdminEnvironment admin = environment.admin();

        features.forEach(jersey::register);

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
        environment.getAdminContext().setAttribute(ServletProperties.SERVICE_LOCATOR, serviceLocator);

        serviceLocator.inject(application);
    }

    private <T> List<T> listServices(Class<T> type) {
        return serviceLocator.getAllServices(type);
    }

    private static class EnvBinder extends AbstractBinder {

        private final Application application;
        private final Environment environment;

        private EnvBinder(Application application, Environment environment) {
            this.application = application;
            this.environment = environment;
        }

        @Override
        protected void configure() {
            bind(application);
            bind(application).to(Application.class);
            bind(environment);
            bind(environment.getObjectMapper());
            bind(environment.metrics());
            bind(environment.getValidator());
        }
    }
}
