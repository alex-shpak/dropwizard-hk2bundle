package net.winterly.dropwizard.hk2bundle;

import net.winterly.dropwizard.hk2bundle.health.ExampleHealthCheck;
import net.winterly.dropwizard.hk2bundle.metric.ExampleGauge;
import net.winterly.dropwizard.hk2bundle.spi.DropwizardBinder;

public class ExampleAppBinder extends DropwizardBinder {

    @Override
    protected void configure() {
        metric(ExampleGauge.class);
        healthCheck(ExampleHealthCheck.class);
    }
}
