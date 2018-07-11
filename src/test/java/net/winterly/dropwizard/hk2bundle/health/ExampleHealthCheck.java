package net.winterly.dropwizard.hk2bundle.health;

import net.winterly.dropwizard.hk2bundle.jdbi.ExampleDAO;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Named("iamalive")
public class ExampleHealthCheck extends com.codahale.metrics.health.HealthCheck {

    @Inject
    private Provider<ExampleDAO> dao;

    public int counter = 0;

    @Override
    protected Result check() {
        counter++;
        return Result.healthy("ok");
    }
}
