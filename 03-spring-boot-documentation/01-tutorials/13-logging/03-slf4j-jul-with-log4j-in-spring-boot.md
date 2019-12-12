# Using SLF4J/JUL with Log4j in Spring Boot

使用 

- SLF4J API or JUL (Java Util Logging) API

-  Log4j implementation 

```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
        <version>1.8.0-alpha2</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jul-to-slf4j</artifactId>
        <version>1.8.0-alpha2</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.8.0-alpha2</version>
    </dependency>
</dependencies>
```

## Using SLF4J API

```
package com.logicbig.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyBean {
  private static final Logger logger = LoggerFactory.getLogger(MyBean.class.getName());

  public void doSomething() {
      logger.info("some message using SLF4J API");
  }
}
```

## Using JUL API

We are also using JUL API with Log4J implementation. This is only for demo purpose, in real application we are likely to use only one API.

```
package com.logicbig.example;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MyBean2 {
  private static final Logger logger = java.util.logging.Logger.getLogger(MyBean2.class.getName());

  public void doSomething() {
      logger.info("some message using JUL API");
  }
}
```

## Log4j configuration

#### src/main/resources/log4j.properties

```
log4j.rootCategory=INFO, stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%d{yy-MMM-dd E HH:mm:ss:SSS}] [%p] [%c{2}:%L] - %m%n
```