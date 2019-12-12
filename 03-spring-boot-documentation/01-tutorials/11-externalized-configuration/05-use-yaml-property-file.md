# 使用 YAML property file

Spring boot支持存储外部属性的YAML格式。我们可以创建应用程序。yml作为application.properties的替代。或者我们甚至可以在同一个应用程序中使用它们。



## Example

#### src/main/resources/application.yml

```
app:
 title: Boot ${app} @project.artifactId@
spring:
    main:
     LogStartupInfo: false
```

在上面的文件中,我们可以指定 app.title 和 spring.main.logStartupInfo 属性,我们可以使用`${}`和`@...@`占位符

#### src/main/resources/application.properties

```
spring.main.banner-mode=off 
```

```java
@SpringBootConfiguration
public class ExampleMain {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication bootApp = new SpringApplication(ExampleMain.class);
        ConfigurableApplicationContext context = bootApp.run(args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();
    }

    private static class MyBean {

        @Value("${app.title}")
        private String appTitle;

        public void doSomething() {
            System.out.printf("App title : %s%n", appTitle);
        }
    }
}
```

输出

```
mvn -q spring-boot:run -Dapp=Example
```

```
2017-07-24 21:24:05.842  INFO 15608 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@436a4e4b: startup date [Mon Jul 24 21:24:05 CDT 2017]; root of context hierarchy
App title : Boot Example yaml-properties
2017-07-24 21:24:06.052  INFO 15608 --- [       Thread-1] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@436a4e4b: startup date [Mon Jul 24 21:24:05 CDT 2017]; root of context hierarchy
```

