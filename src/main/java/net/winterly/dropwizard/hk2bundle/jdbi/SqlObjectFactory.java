package net.winterly.dropwizard.hk2bundle.jdbi;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InstantiationService;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class SqlObjectFactory implements Factory<Object> {

    private final InstantiationService instantiationService;
    private final Provider<DBI> dbi;

    @Inject
    public SqlObjectFactory(InstantiationService instantiationService, Provider<DBI> dbi) {
        this.instantiationService = instantiationService;
        this.dbi = dbi;
    }

    @Override
    public Object provide() {
        Injectee injectee = instantiationService.getInstantiationData().getParentInjectee();
        Class<?> daoInterface = (Class) injectee.getRequiredType();

        return dbi.get().onDemand(daoInterface);
    }

    @Override
    public void dispose(Object instance) {
        dbi.get().close(instance);
    }
}
