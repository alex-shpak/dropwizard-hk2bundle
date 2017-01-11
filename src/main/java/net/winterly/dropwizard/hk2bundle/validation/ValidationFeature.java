package net.winterly.dropwizard.hk2bundle.validation;

import io.dropwizard.jersey.validation.DropwizardConfiguredValidator;
import io.dropwizard.jersey.validation.Validators;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.ConfiguredValidator;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validator;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class ValidationFeature implements Feature {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public boolean configure(FeatureContext context) {

        Validator validator = Validators
                .newConfiguration()
                .constraintValidatorFactory(new InjectingValidatorFactory())
                .buildValidatorFactory()
                .getValidator();

        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new DropwizardConfiguredValidator(validator)).to(ConfiguredValidator.class);
            }
        });

        return true;
    }

    private class InjectingValidatorFactory implements ConstraintValidatorFactory {

        @Override
        public final <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            return serviceLocator.createAndInitialize(key);
        }

        @Override
        public void releaseInstance(ConstraintValidator<?, ?> instance) {

        }
    }
}