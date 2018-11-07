## Dropwizard hk2 Bundle
Dropwizard bundle for injection of managed objects, tasks etc using hk2 integration


## Motivation
HK2 is default DI container in jersey, which is widely used in dropwizard, therefore it seems logical to use single DI container.


## Features
 - Registration and injection for:
   - Healthchecks
   - Managed objects
   - Lifecycle listeners
   - Tasks
   - Commands
   - Metrics
   - Other bundles
 - Jdbi DAOs injections 
 - Support for injections before Jersey initialisation
 
 
## Usage
Grab latest version from [JitPack](https://jitpack.io/#alex-shpak/dropwizard-hk2bundle)  

#### Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
```groovy
dependencies {
    compile 'com.github.alex-shpak:dropwizard-hk2bundle:0.8.0'
}
```


#### Code
Add bundle to your dropwizard application
```java
ExampleAppBinder appBinder = new ExampleAppBinder();

HK2Bundle hk2Bundle = new HK2Bundle(appBinder);
bootstrap.addBundle(hk2Bundle);
```

Register `DatabaseHealthCheck` in HK2 binder
```java
public class ExampleAppBinder extends DropwizardBinder {

    @Override
    protected void configure() {
        healthCheck(DatabaseHealthCheck.class);
    }
}
```

`DropwizardBinder` is convenience class that contains `register()` method to register objects specific for dropwizard
So now `DatabaseHealthCheck` get created and initialized by DI container

```java
@Named("database") //Health check name. Class name will be used if not set 
public class DatabaseHealthCheck extends HealthCheck {

    @Inject 
    private Provider<Database> database;

    @Override
    protected Result check() throws Exception {
        if(database.get().isConnected()) {
            return Result.healthy();
        }
        return Result.unhealthy("Not connected");
    }
}
```



#### JDBI DAO Injection
Considering you already have `dropwizard-jdbi` module in dependencies, add `JDBIBinder` to `HK2Bundle` configuration.
Then you would be able to inject DAOs.

```java
ExampleAppBinder appBinder = new ExampleAppBinder();
JDBIBinder jdbiBinder = new JDBIBinder<ExampleAppConfiguration>(configuration -> configuration.database)
        // .setDBIFactory(JDBIFactory.class)
        // .setSqlObjectFactory(SqlObjectFactory.class)
        .register(ExampleDAO.class);


HK2Bundle hk2Bundle = new HK2Bundle(appBinder, jdbiBinder);
bootstrap.addBundle(hk2Bundle);
```
```java
@Inject
private ExampleDAO exampleDAO;
```


## Licence
[MIT](LICENSE)
