package net.winterly.dropwizard.hk2bundle.jdbi;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Self;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.Set;

import static org.glassfish.hk2.utilities.reflection.ReflectionHelper.getFirstTypeArgument;
import static org.glassfish.hk2.utilities.reflection.ReflectionHelper.getRawClass;

/**
 * Default factory for creating JDBI sql objects. This factory uses active descriptor to find sql interface type.
 */
@Singleton
public class SqlObjectFactory implements Factory<Object> {

    private final Provider<Jdbi> jdbi;
    private final ActiveDescriptor<Factory> activeDescriptor;

    @Inject
    public SqlObjectFactory(Provider<Jdbi> dbi, @Self ActiveDescriptor<Factory> activeDescriptor) {
        this.activeDescriptor = activeDescriptor;
        this.jdbi = dbi;
    }

    @Override
    public Object provide() {
        Class<?> daoInterface = extractDaoType(activeDescriptor);
        return jdbi.get().onDemand(daoInterface);
    }

    @Override
    public void dispose(Object instance) {

    }

    /**
     * Extract SQL Interface type from {@link ActiveDescriptor} and generic {@link Factory} type
     *
     * @param activeDescriptor self descriptor
     * @return sql object type or throws exception
     */
    private Class<?> extractDaoType(ActiveDescriptor<Factory> activeDescriptor) {
        Set<Type> contracts = activeDescriptor.getContractTypes();

        for (Type type : contracts) {
            Class raw = getRawClass(type);
            if (Factory.class.isAssignableFrom(raw)) {
                return (Class<?>) getFirstTypeArgument(type);
            }
        }

        throw new IllegalStateException("Unable to resolve sql interface type");
    }
}
