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
 - Hibernate validators injections
 - Jdbi (version 2) DAOs injections 
 - Support for injections before Jersey initialisation
 
 
## Usage
#### Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
```groovy
dependencies {
    compile 'com.github.alex-shpak:dropwizard-hk2bundle:0.6.0'
}
```


#### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.github.alex-shpak</groupId>
        <artifactId>dropwizard-hk2bundle</artifactId>
        <version>0.6.0</version>
    </dependency>
</dependencies>
```


#### Code
Add bundle to your dropwizard application
```java
bootstrap.addBundle(HK2Bundle.builder()
        .bind(new Binder())
        .build()
);
```

Register `DatabaseHealthCheck` in HK2 binder
```java
public class Binder extends DropwizardBinder {

    @Override
    protected void configure() {
        register(DatabaseHealthCheck.class);
    }
}
```

`DropwizardBinder` is convenience class that contains `register()` method to register objects specific for dropwizard
So now `DatabaseHealthCheck` get created and initialized by DI container

```java
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
Considering you already has `dropwizard-jdbi` module in dependencies, add `JDBIBinder` to `HK2Bundle` configuration.
Then you would be able to inject DAOs.

```java
HK2Bundle hk2bundle = HK2Bundle.builder()
        .jdbi(config -> config.database, Configuration.class)
        .build();
bootstrap.addBundle(hk2bundle);
```
```java
@InjectDAO
private MyDAO myDAO;
```


## How it works
Hk2bundle initializes new `ServiceLocator` and binds found services into it.
Then it sets created `ServiceLocator` as parent of jersey's `ServiceLocator`
```java
environment.getApplicationContext().setAttribute(
    ServletProperties.SERVICE_LOCATOR, serviceLocator
);
```

After jersey initialisation services (if enabled) will be re-injected with new `ServiceLocator`


## Licence
[MIT](LICENSE)
