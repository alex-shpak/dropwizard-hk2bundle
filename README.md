### Dropwizard hk2 Bundle
Simple dropwizard bundle for autodiscovery and injection of dropwizard managed objects, tasks etc using hk2 integration

### Features
 - Auto discovery for:
   - Healthchecks
   - Managed objects
   - Lifecycle listeners
   - Tasks
   - Commands
   - Other bundles
 - Hibernate validators injections
 - Jdbi DAOs injections 
 - Support for injections before Jersey initialisation
 
### Usage
#### Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
```groovy
dependencies {
    compile 'com.github.alex-shpak:dropwizard-hk2bundle:0.3.0'
    compile 'org.glassfish.hk2:hk2-metadata-generator:2.4.0'
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
        <version>0.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.glassfish.hk2</groupId>
        <artifactId>hk2-metadata-generator</artifactId>
        <version>2.4.0</version>
    </dependency>
</dependencies>
```
#### Code
Add bundle to your dropwizard application
```java
bootstrap.addBundle(new HK2Bundle(this));
```
All classes that you want to be discovered or injected should have `@org.jvnet.hk2.annotations.Service` annotation

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

#### JDBI DAO Injection
Add `dropwizard-jdbi` module to your dependencies and use `@InjectDAO` annotation 
```java
@InjectDAO
private PiwikDAO piwikDAO;
```
Make sure that `DataSourceFactory` is registered in `ServiceLocator` for example via factory
```java
public class DSFFactory implements Factory<DataSourceFactory> {

    @Inject
    private Configuration configuration;

    @Override
    public DataSourceFactory provide() {
        return configuration.getDataSourceFactory();
    }

    @Override
    public void dispose(DataSourceFactory instance) {

    }
}
```
```java
@Service
public class InjectionBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(DSFFactory.class).to(DataSourceFactory.class).in(Singleton.class);
    }
}
```

### How it works
Hk2bundle initializes new `ServiceLocator` and binds found services into it.
Then it sets created `ServiceLocator` as parent of jersey's `ServiceLocator`
```java
environment.getApplicationContext().setAttribute(
    ServletProperties.SERVICE_LOCATOR, serviceLocator
);
```

After jersey initialisation services will be re-injected with new `ServiceLocator`


### Without hk2 metadata generator
If you don't want to use metadata generator you can bind services manually using `AbstractBinder` supplied to bundle constructor

```java
public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(DatabaseHealthCheck.class);
    }
}
```
```java
bootstrap.addBundle(new HK2Bundle(this, new Binder()));
```

### Licence
[MIT](LICENCE)