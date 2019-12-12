# Binding Command line Json Properties using @ConfigurationProperties

This example shows how to bind [Command line JSON properties](https://www.logicbig.com/tutorials/spring-framework/spring-boot/json-env-properties.html) using [@ConfigurationProperties](https://www.logicbig.com/tutorials/spring-framework/spring-boot/configuration-properties.html).

# Example

## Using @ConfigurationProperties

```java
@Component
@ConfigurationProperties("app")
public class AppSettings{
  private String title;
  private boolean active;
    .............
}
```



```java

@Component
public class ClientBean {
  @Autowired
  private AppSettings appSettings;

  @PostConstruct
  private void postConstruct() {
      System.out.println(appSettings);
  }
}
```

```
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) {
      SpringApplication.run(ExampleMain.class, args);
  }
}
```

## Supplying JSON via System Properties

Using spring-boot maven plugin:

```
D:\boot-json-env-with-configuration-props>mvn -q -Dspring.application.json="{\"app\":{\"title\":\"test\",\"active\":true}}"  spring-boot:run
AppSettings{title='test', active=true}
```

Packaging:

```
D:\example-projects\spring-boot\boot-json-env-with-configuration-props>mvn -q package spring-boot:repackage
```

Executing jar

```
D:\boot-json-env-with-configuration-props>java -Dspring.application.json="{\"app\":{\"title\":\"test\",\"active\":true}}"  -jar target/boot-json-env-with-configuration-props-1.0-SNAPSHOT.jar
AppSettings{title='test', active=true}
```

## Supplying JSON via application argument

```
D:\boot-json-env-with-configuration-props>java -jar target/boot-json-env-with-configuration-props-1.0-SNAPSHOT.jar --spring.application.json="{\"app\":{\"title\":\"test\",\"active\":true}}"
AppSettings{title='test', active=true}
```

## Supplying JSON via Environment Variable

Setting environment variable from command line in windows:

```
D:\example-projects\spring-boot\boot-json-env-with-configuration-props>set SPRING_APPLICATION_JSON={"app":{"title":"test","active":true}}
D:\boot-json-env-with-configuration-props>mvn spring-boot:run
AppSettings{title='test', active=true}
D:\boot-json-env-with-configuration-props>java -jar target/boot-json-env-with-configuration-props-1.0-SNAPSHOT.jar
AppSettings{title='test', active=true}
```

On Linux we can set environment variable from command line as:

```
$ export SPRING_APPLICATION_JSON="{\"app\":{\"title\":\"test\",\"active\":true}}"
```