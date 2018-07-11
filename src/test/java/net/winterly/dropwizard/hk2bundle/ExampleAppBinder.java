package net.winterly.dropwizard.hk2bundle;

import net.winterly.dropwizard.hk2bundle.health.ExampleHealthCheck;
import net.winterly.dropwizard.hk2bundle.jdbi.ExampleDAO;
import net.winterly.dropwizard.hk2bundle.jdbi.SqlObjectFactory;
import net.winterly.dropwizard.hk2bundle.metric.ExampleGauge;
import net.winterly.dropwizard.hk2bundle.spi.DropwizardBinder;
import net.winterly.dropwizard.hk2bundle.validation.InjectValidatorBundle;

import javax.inject.Singleton;

public class ExampleAppBinder extends DropwizardBinder {
    @Override
    protected void configure() {
        bundle(InjectValidatorBundle.class);

        metric(ExampleGauge.class);
        healthCheck(ExampleHealthCheck.class);

        bindFactory(SqlObjectFactory.class)
                .to(ExampleDAO.class)
                .in(Singleton.class);
    }
}
