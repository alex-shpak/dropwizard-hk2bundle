package net.winterly.dropwizard.hk2bundle.validation;

import io.dropwizard.Bundle;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;

public class HK2ValidationBundle implements Bundle {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(Environment environment) {
        environment.setValidator(Validators
                .newConfiguration()
                .constraintValidatorFactory(serviceLocator.getService(InjectingValidatorFactory.class))
                .buildValidatorFactory()
                .getValidator());
    }
}
