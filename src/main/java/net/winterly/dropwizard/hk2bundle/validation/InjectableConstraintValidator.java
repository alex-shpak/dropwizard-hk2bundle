package net.winterly.dropwizard.hk2bundle.validation;

import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public abstract class InjectableConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        serviceLocator.inject(this);
        return isValid(value, context, serviceLocator);
    }

    public abstract boolean isValid(T value, ConstraintValidatorContext context, ServiceLocator serviceLocator);
}
