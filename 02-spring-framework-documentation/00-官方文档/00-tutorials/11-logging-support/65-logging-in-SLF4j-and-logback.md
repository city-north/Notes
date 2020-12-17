https://mp.weixin.qq.com/s/WDYlUYu09s4Dagv0ChK0Gw

## 使用 SLF4j (Simple Logging Facade for Java)

SLF4J  和 JCL 很像,但是有很多优势,[here](https://www.logicbig.com/tutorials/misc/java-logging/slf4j.html)

本例子展示如何使用 SLF4J,通过关闭默认的 JCL,我们会使用 Log4j

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>4.3.10.RELEASE</version>
          <exclusions>
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.10.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>1.8.0-alpha2</version>
        </dependency>
 </dependencies>
```

## Bean 中使用

```java
package com.logicbig.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBean {
  private static Logger log = LoggerFactory.getLogger(MyBean.class);

  public void doSomething() {
      log.info("doing something");
  }
}
```

#### 配置文件

src\main\resources\logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yy-MM-dd HH:mm:ss:SSS} %5p %t %c{2}:%L - %m%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="stdout"/>
    </root>
</configuration>
```

### Main class

```java
@Configuration
public class ExampleMain {

  @Bean
  public MyBean myBean() {
      return new MyBean();
  }

  public static void main(String[] args) {
      ConfigurableApplicationContext context =
              new AnnotationConfigApplicationContext(ExampleMain.class);
      MyBean bean = context.getBean(MyBean.class);
      bean.doSomething();
  }
}
```

```
17-09-10 20:40:23:977  INFO main o.s.c.a.AnnotationConfigApplicationContext:583 - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@2c8d66b2: startup date [Sun Sep 10 20:40:23 CDT 2017]; root of context hierarchy
17-09-10 20:40:24:140  INFO main c.l.e.MyBean:10 - doing something
```

