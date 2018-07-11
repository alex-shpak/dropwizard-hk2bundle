package net.winterly.dropwizard.hk2bundle;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExampleAppConfiguration extends Configuration {

    @Valid
    @NotNull
    public DataSourceFactory database;
}
