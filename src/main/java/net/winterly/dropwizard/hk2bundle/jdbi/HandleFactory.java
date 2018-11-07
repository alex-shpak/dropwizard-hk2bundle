package net.winterly.dropwizard.hk2bundle.jdbi;

import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Creates {@link Handle} instances for request and automatically closes them at the end
 */
@Singleton
public class HandleFactory implements Factory<Handle> {

    private final Provider<Jdbi> jdbi;

    @Inject
    public HandleFactory(Provider<Jdbi> jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    @RequestScoped
    public Handle provide() {
        return jdbi.get().open();
    }

    @Override
    public void dispose(Handle instance) {
        instance.close();
    }
}
