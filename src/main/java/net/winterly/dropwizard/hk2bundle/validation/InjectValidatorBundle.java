package net.winterly.dropwizard.hk2bundle.validation;

import io.dropwizard.Bundle;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ValidatorFactory;
import javax.ws.rs.container.ResourceContext;

public class InjectValidatorBundle implements Bundle {

    private ConstraintValidatorFactory validatorFactory = new ConstraintValidatorFactoryImpl();

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        ValidatorFactory validatorFactory = Validators.newConfiguration()
                .constraintValidatorFactory(new InjectingValidatorFactory())
                .buildValidatorFactory();

        bootstrap.setValidatorFactory(validatorFactory);
    }

    @Override
    public void run(Environment environment) {
        GetLocatorFeature getLocatorFeature = new GetLocatorFeature(serviceLocator -> {
            ResourceContext resourceContext = serviceLocator.getService(ResourceContext.class);
            validatorFactory = resourceContext.getResource(InjectingConstraintValidatorFactory.class);
        });
        environment.jersey().register(getLocatorFeature);
    }

    private class InjectingValidatorFactory implements ConstraintValidatorFactory {

        @Override
        public final <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            return validatorFactory.getInstance(key);
        }

        @Override
        public void releaseInstance(ConstraintValidator<?, ?> instance) { }
    }
}
