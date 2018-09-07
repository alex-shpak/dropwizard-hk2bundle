package net.winterly.dropwizard.hk2bundle.jdbi;

import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Creates {@link Handle} instances for request and automatically closes them at the end
 */
@Singleton
public class HandleFactory implements Factory<Handle> {

    private final Provider<DBI> dbi;

    @Inject
    public HandleFactory(Provider<DBI> dbi) {
        this.dbi = dbi;
    }

    @Override
    @RequestScoped
    public Handle provide() {
        return dbi.get().open();
    }

    @Override
    public void dispose(Handle instance) {
        instance.close();
    }
}
