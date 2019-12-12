# 从命令行中解析 JSON 属性

我们可以使用以下三种方式输入命令行的 json

- System property spring.application.json
- Application argument --spring.application.json
- Environment variable SPRING_APPLICATION_JSON

# Example

## Client Bean

```java
package com.logicbig.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class ClientBean {
  @Value("${title}")
  private String title;
  @Value("${active}")
  private boolean active;

  @PostConstruct
  private void postConstruct() {
      System.out.printf("title= %s, active= %s%n", title, active);
  }
}
```

## Main class

```java
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

## Supplying JSON via System Properties

### Using spring maven plugin:

```
boot-json-env-properties>mvn -q -Dspring.application.json="{\"title\":\"test\",\"active\":true}" spring-boot:run
title= test, active= true
```

### Executing Jar

```
boot-json-env-properties>mvn -q package spring-boot:repackage
```

```
D:\boot-json-env-properties>java -Dspring.application.json="{\"title\":\"test\",\"active\":true}"  -jar target\boot-json-env-properties-1.0-SNAPSHOT.jar
title= test, active= true
```

## Supplying JSON via application argument

```
D:\boot-json-env-properties>java -jar  target\boot-json-env-properties-1.0-SNAPSHOT.jar --spring.application.json="{\"title\":\"test\",\"active\":true}"
title= test, active= true
```

## Supplying JSON via Environment Variable

```
D:\boot-json-env-properties>set SPRING_APPLICATION_JSON={"title":"test","active":true}
```

```
D:\boot-json-env-properties>mvn -q spring-boot:run
title= test, active= true
```

```
D:\boot-json-env-properties>java -jar  target\boot-json-env-properties-1.0-SNAPSHOT.jar
title= test, active= true
```

On Linux we can set environment variable from command line as:

```
$ export SPRING_APPLICATION_JSON="{\"title\":\"test\",\"active\":true}"
```