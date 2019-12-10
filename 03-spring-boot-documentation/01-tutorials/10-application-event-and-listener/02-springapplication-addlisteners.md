# Using SpringApplication.addListeners()

在 SpringBoot 应用中,通过`SpringApplication.addListeners()`或者`SpringApplicationBuilder.listeners()`可以让我们在 ApplicationContext 创建之前,监听应用的事件

## 实例

```java
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
      System.out.println("event: " + event);
  }
}
```



```java
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      SpringApplication sa = new SpringApplication();
      sa.addListeners(new MyApplicationListener());
      sa.setSources(new HashSet<>(Arrays.asList(ExampleMain.class)));
      ConfigurableApplicationContext context = sa.run(args);
      MyBean bean = context.getBean(MyBean.class);
      bean.doSomething();
  }
}
```

输出

```
event: org.springframework.boot.context.event.ApplicationStartedEvent[source=org.springframework.boot.SpringApplication@402f32ff]
event: org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent[source=org.springframework.boot.SpringApplication@402f32ff]
event: org.springframework.boot.context.event.ApplicationPreparedEvent[source=org.springframework.boot.SpringApplication@402f32ff]
2017-09-07 11:36:24.881  INFO 9556 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 11:36:24 CDT 2017]; root of context hierarchy
2017-09-07 11:36:25.274  INFO 9556 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
event: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 11:36:24 CDT 2017]; root of context hierarchy]
event: org.springframework.boot.context.event.ApplicationReadyEvent[source=org.springframework.boot.SpringApplication@402f32ff]
-- in doSomething() --
2017-09-07 11:36:25.284  INFO 9556 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 11:36:24 CDT 2017]; root of context hierarchy
event: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@7e9a5fbe: startup date [Thu Sep 07 11:36:24 CDT 2017]; root of context hierarchy]
2017-09-07 11:36:25.285  INFO 9556 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
```

四个SpringBoot的事件:

- ApplicationStartedEvent
  - 在 SpringApplicaton刚启动就发送,
  - 在 Environment 初始化之前
  - 在 ApplicationContext 初始化之前
  - 在ApplicationListeners注册完以后
- ApplicationEnvironmentPreparedEvent
  - 在 Environment 初始化后,完全可以查看和修改
- ApplicationPreparedEvent
  - 在 ApplicationContext 完全初始化完但是没有刷新之前
- ApplicationReadyEvent
  - 应用启动完毕可以接收请求后发送
- ApplicationFailedEvent
  - 如果应用启动失败发送