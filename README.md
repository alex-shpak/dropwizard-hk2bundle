## Dropwizard hk2 Bundle
Simple dropwizard bundle for autodiscovery and injection of dropwizard managed objects, tasks etc using hk2 integration


## Features
 - Auto registration and injection for:
   - Healthchecks
   - Managed objects
   - Lifecycle listeners
   - Tasks
   - Commands
   - HK2 Binders
   - Other bundles
 - Hibernate validators injections
 - Jdbi DAOs injections 
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
    compile 'com.github.alex-shpak:dropwizard-hk2bundle:0.5.4'
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
        <version>0.5.4</version>
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

All classes that you want to be discovered or injected should be annotated with `@org.jvnet.hk2.annotations.Service` annotation

```java
@Service
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

Register `DatabaseHealthCheck` in HK2
```java
public class Binder extends DropwizardBinder {

    @Override
    protected void configure() {
        register(DatabaseHealthCheck.class);
    }
}
```
`DropwizardBinder` is convenience class that contains `register()` method to register objects specific for dropwizard


#### JDBI DAO Injection
Considering you already has `dropwizard-jdbi` module in dependencies, add `JDBIBinder` to `HK2Bundle` configuration.
Then you would be able to inject DAOs.

```java
HK2Bundle hk2bundle = HK2Bundle.builder()
        .withJDBI(config -> config.database)
        .build();
bootstrap.addBundle(hk2bundle);
```
```java
@Inject
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
[MIT](LICENCE)
