package net.winterly.dropwizard.hk2bundle.jdbi;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InstantiationService;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;

public class SqlObjectFactory implements Factory<Object> {

    @Inject
    private InstantiationService instantiationService;

    @Inject
    private DBI dbi;

    @Override
    public Object provide() {
        Injectee injectee = instantiationService.getInstantiationData().getParentInjectee();
        Class<?> daoInterface = (Class) injectee.getRequiredType();
        return dbi.onDemand(daoInterface);
    }

    @Override
    public void dispose(Object instance) {
        dbi.close(instance);
    }
}
