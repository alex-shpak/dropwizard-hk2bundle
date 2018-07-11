package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.dropwizard.hk2bundle.jdbi.JDBIBinder;
import net.winterly.dropwizard.hk2bundle.jdbi.JDBIFactory;
import net.winterly.dropwizard.hk2bundle.jdbi.SqlObjectFactory;
import net.winterly.dropwizard.hk2bundle.resource.ExampleResource;

public class ExampleApp extends Application<ExampleAppConfiguration> {

    @Override
    public void initialize(Bootstrap<ExampleAppConfiguration> bootstrap) {
        JDBIBinder jdbiBinder = new JDBIBinder<ExampleAppConfiguration>()
                .setDBIFactory(JDBIFactory.class)
                .setSqlObjectFactory(SqlObjectFactory.class)
                .setDatabaseConfiguration(configuration -> configuration.database);

        HK2Bundle hk2Bundle = new HK2Bundle(
                new ExampleAppBinder(),
                jdbiBinder
        );
        bootstrap.addBundle(hk2Bundle);
    }

    @Override
    public void run(ExampleAppConfiguration configuration, Environment environment) {
        environment.jersey().register(ExampleResource.class);
    }

    public static void main(String[] args) throws Exception {
        ExampleApp app = new ExampleApp();
        app.run(args);
    }
}
