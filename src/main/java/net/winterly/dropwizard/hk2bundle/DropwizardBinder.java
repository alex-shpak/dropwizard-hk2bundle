package net.winterly.dropwizard.hk2bundle;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.servlets.tasks.Task;
import org.eclipse.jetty.util.component.LifeCycle;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

public abstract class DropwizardBinder extends AbstractBinder {

    private static final List<Class<?>> dropwizardContracts = Arrays.asList(
            Bundle.class,
            ConfiguredBundle.class,
            Command.class,

            HealthCheck.class,
            Managed.class,
            LifeCycle.class,
            LifeCycle.Listener.class,
            ServerLifecycleListener.class,
            Task.class
    );

    /**
     * Binds dropwizard specific service class as singleton
     * <p>
     * If service type is not one of dropwizard contracts just {@link AbstractBinder#bind(Class)} will be invoked
     *
     * @param serviceType dropwizard service class
     * @param <T>         service type
     * @return builder for further modification
     */
    public <T> ServiceBindingBuilder<T> register(Class<T> serviceType) {
        ServiceBindingBuilder<T> builder = super.bindAsContract(serviceType);

        for (Class<?> contract : dropwizardContracts) {
            if (contract.isAssignableFrom(serviceType)) {
                builder.to(contract).in(Singleton.class);
            }
        }

        return builder;
    }
}
