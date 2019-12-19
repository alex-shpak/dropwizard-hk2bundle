package net.winterly.dropwizard.hk2bundle.spi;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.servlets.tasks.Task;
import org.eclipse.jetty.util.component.LifeCycle;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.ScopedBindingBuilder;

import javax.inject.Singleton;

/**
 * Dropwizard-specific {@link AbstractBinder} for classes and instances of managed types
 */
@SuppressWarnings("unused")
public abstract class DropwizardBinder extends AbstractBinder {

    public <T extends Bundle> ScopedBindingBuilder<T> bundle(Class<T> bundle) {
        return bindAsContract(bundle)
                .to(Bundle.class)
                .in(Singleton.class);
    }

    public <T extends Bundle> ScopedBindingBuilder<T> bundle(T bundle) {
        return bind(bundle)
                .to(Bundle.class);
    }

    public <T extends ConfiguredBundle<?>> ScopedBindingBuilder<T> configuredBundle(Class<T> configuredBundle) {
        return bindAsContract(configuredBundle)
                .to(ConfiguredBundle.class)
                .in(Singleton.class);
    }

    public <T extends ConfiguredBundle<?>> ScopedBindingBuilder<T> configuredBundle(T configuredBundle) {
        return bind(configuredBundle)
                .to(ConfiguredBundle.class);
    }

    public <T extends Command> ScopedBindingBuilder<T> command(Class<T> command) {
        return bindAsContract(command)
                .to(Command.class)
                .in(Singleton.class);
    }

    public <T extends HealthCheck> ScopedBindingBuilder<T> healthCheck(Class<T> healthCheck) {
        return bindAsContract(healthCheck)
                .to(HealthCheck.class)
                .in(Singleton.class);
    }

    public <T extends Metric> ScopedBindingBuilder<T> metric(Class<T> metric) {
        return bindAsContract(metric)
                .to(Metric.class)
                .in(Singleton.class);
    }

    public <T extends MetricSet> ScopedBindingBuilder<T> metricSet(Class<T> metricSet) {
        return bindAsContract(metricSet)
                .to(MetricSet.class)
                .in(Singleton.class);
    }

    public <T extends Managed> ScopedBindingBuilder<T> managed(Class<T> managed) {
        return bindAsContract(managed)
                .to(Managed.class)
                .in(Singleton.class);
    }

    public <T extends LifeCycle> ScopedBindingBuilder<T> lifeCycle(Class<T> lifeCycle) {
        return bindAsContract(lifeCycle)
                .to(LifeCycle.class)
                .in(Singleton.class);
    }

    public <T extends LifeCycle.Listener> ScopedBindingBuilder<T> lifeCycleListener(Class<T> lifeCycleListener) {
        return bindAsContract(lifeCycleListener)
                .to(LifeCycle.Listener.class)
                .in(Singleton.class);
    }

    public <T extends ServerLifecycleListener> ScopedBindingBuilder<T> serverLifecycleListener(Class<T>
                                                                                                       serverLifecycleListener) {
        return bindAsContract(serverLifecycleListener)
                .to(ServerLifecycleListener.class)
                .in(Singleton.class);
    }

    public <T extends Task> ScopedBindingBuilder<T> task(Class<T> task) {
        return bindAsContract(task)
                .to(Task.class)
                .in(Singleton.class);
    }

    public <T extends Task> ScopedBindingBuilder<T> task(T task) {
        return bind(task).to(Task.class);
    }
}
