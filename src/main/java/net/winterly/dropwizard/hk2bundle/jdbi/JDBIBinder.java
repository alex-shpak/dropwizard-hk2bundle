package net.winterly.dropwizard.hk2bundle.jdbi;

import io.dropwizard.jdbi.InjectDAO;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;

public class JDBIBinder extends AbstractBinder {

    private final DataSourceFactoryProvider dataSourceFactoryProvider;

    public JDBIBinder(DataSourceFactoryProvider dsfProvider) {
        this.dataSourceFactoryProvider = dsfProvider;
    }

    @Override
    protected void configure() {
        bind(dataSourceFactoryProvider);

        bindFactory(JDBIFactory.class)
                .to(DBI.class)
                .in(Singleton.class);

        bind(JDBIInjectionResolver.class)
                .to(new TypeLiteral<InjectionResolver<InjectDAO>>() {})
                .in(Singleton.class);
    }
}
