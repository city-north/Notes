# 使用 Log4j2

```java
/**
 * 代码实例 log4j2 使用
 *
 * @author EricChen 2019/11/29 13:32
 */
@Configuration
public class Log4j2Example {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(Log4j2Example.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    public static class MyBean {
        private static Log log = LogFactory.getLog(MyBean.class);

        public void doSomething() {
            log.info("doing something");
        }
    }
}

```

## 依赖

```xml
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-core</artifactId>
   <version>2.8.2</version>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-jcl</artifactId>
   <version>2.8.2</version>
</dependency>
```

## Log4j2.properties

```
status = error
name = PropertiesConfig
filters = threshold

filter.threshold.type = ThresholdFilter
filter.threshold.level = info

appenders = console

appender.console.type = Console
appender.console.name = STDOUT
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = %d{yy-MM-dd HH:mm:ss:SSS} %-5p %c{1}:%L - %m%n

rootLogger.level = info
rootLogger.appenderRefs = stdout
rootLogger.appenderRef.stdout.ref = STDOUT
```

