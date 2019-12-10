# 使用 spring.factories 注册 ApplicationListener

- 使用 META-INF/spring.factories 添加监听器

## 代码实例

```java
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
      System.out.println("event: " + event);
  }
}
```

#### src/main/resources/META-INF/spring.factories

```
org.springframework.context.ApplicationListener=com.logicbig.example.MyApplicationListener
```

```
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      ConfigurableApplicationContext context =
              SpringApplication.run(ExampleMain.class, args);
      MyBean bean = context.getBean(MyBean.class);
      bean.doSomething();
  }
}
```

```
event: org.springframework.boot.context.event.ApplicationStartedEvent[source=org.springframework.boot.SpringApplication@402f32ff]
event: org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent[source=org.springframework.boot.SpringApplication@402f32ff]
event: org.springframework.boot.context.event.ApplicationPreparedEvent[source=org.springframework.boot.SpringApplication@402f32ff]
2017-09-07 12:25:02.426  INFO 18760 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 12:25:02 CDT 2017]; root of context hierarchy
2017-09-07 12:25:02.826  INFO 18760 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
event: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 12:25:02 CDT 2017]; root of context hierarchy]
event: org.springframework.boot.context.event.ApplicationReadyEvent[source=org.springframework.boot.SpringApplication@402f32ff]
-- in doSomething() --
2017-09-07 12:25:02.835  INFO 18760 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 12:25:02 CDT 2017]; root of context hierarchy
event: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 12:25:02 CDT 2017]; root of context hierarchy]
2017-09-07 12:25:02.836  INFO 18760 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
```