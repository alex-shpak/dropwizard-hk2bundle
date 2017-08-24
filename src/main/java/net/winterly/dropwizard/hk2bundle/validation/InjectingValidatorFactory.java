package net.winterly.dropwizard.hk2bundle.validation;

import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

class InjectingValidatorFactory implements ConstraintValidatorFactory {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public final <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return serviceLocator.createAndInitialize(key);  // happens once in lifecycle
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {

    }
}
