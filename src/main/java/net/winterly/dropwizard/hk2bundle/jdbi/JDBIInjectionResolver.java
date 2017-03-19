package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.jdbi.InjectDAO;
import org.glassfish.hk2.api.*;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class JDBIInjectionResolver implements InjectionResolver<InjectDAO> {

    @Inject
    private Provider<DBI> dbiProvider;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        try {
            Class<?> type = (Class<?>) injectee.getRequiredType();
            return dbiProvider.get().onDemand(type);
        } catch (Exception e) {
            throw new MultiException(new UnsatisfiedDependencyException(injectee));
        }
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }

}
