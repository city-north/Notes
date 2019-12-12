# Profile Specific Properties

加载指定的 properties 文件

- `-Dspring.profiles.active=myProfileName.`就会去寻找`application-myProfileName.properties`
- 默认的配置文件是`application.properties`或者`application-default.properties`

# Example

## The default property file

#### src/main/resources/application.properties

```
app.window.width=500
app.window.height=400
```

## Profile specific files

#### src/main/resources/application-dev.properties

```
app.window.height=300
```

#### src/main/resources/application-prod.properties

```
app.window.width=600
app.window.height=700
```

## Example Application

```java
package com.logicbig.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class ClientBean {
  @Value("${app.window.width}")
  private int width;
  @Value("${app.window.height}")
  private int height;

  @PostConstruct
  private void postConstruct() {
      System.out.printf("width= %s, height= %s%n", width, height);
  }
}
package com.logicbig.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) {
      SpringApplication.run(ExampleMain.class, args);
  }
}
```

## Running

```java
$ mvn spring-boot:run

width= 500, height= 400
$ mvn -Dspring.profiles.active=dev spring-boot:run

width= 500, height= 300
$ mvn -Dspring.profiles.active=prod spring-boot:run

width= 600, height= 700
```