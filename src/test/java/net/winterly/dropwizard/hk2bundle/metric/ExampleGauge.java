package net.winterly.dropwizard.hk2bundle.metric;

import com.codahale.metrics.Gauge;
import net.winterly.dropwizard.hk2bundle.health.ExampleHealthCheck;

import javax.inject.Inject;
import javax.inject.Named;

@Named("exampleGauge")
public class ExampleGauge implements Gauge<Integer> {

    @Inject
    private ExampleHealthCheck healthCheck;

    @Override
    public Integer getValue() {
        return healthCheck.counter;
    }
}
