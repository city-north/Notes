# Listening to Application Events with @EventListener

使用@EventListener监听应用事件

```java
@Component
public class ListenerBean {
  @EventListener
  public void handleEvent(Object event) {
      System.out.println("event: "+event);
  }
}
```

```java
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      SpringApplication sa = new SpringApplication(ExampleMain.class);
      ConfigurableApplicationContext context = sa.run(args);
      MyBean bean = context.getBean(MyBean.class);
      bean.doSomething();
  }
}
```

```
2017-09-07 11:38:38.988  INFO 8212 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@6d3af739: startup date [Thu Sep 07 11:38:38 CDT 2017]; root of context hierarchy
2017-09-07 11:38:39.370  INFO 8212 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
event: org.springframework.context.event.ContextRefreshedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@6d3af739: startup date [Thu Sep 07 11:38:38 CDT 2017]; root of context hierarchy]
event: org.springframework.boot.context.event.ApplicationReadyEvent[source=org.springframework.boot.SpringApplication@14fc5f04]
-- in doSomething() --
2017-09-07 11:38:39.379  INFO 8212 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@6d3af739: startup date [Thu Sep 07 11:38:38 CDT 2017]; root of context hierarchy
event: org.springframework.context.event.ContextClosedEvent[source=org.springframework.context.annotation.AnnotationConfigApplicationContext@6d3af739: startup date [Thu Sep 07 11:38:38 CDT 2017]; root of context hierarchy]
2017-09-07 11:38:39.380  INFO 8212 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
```

上例子中,事件`ApplicationReadyEvent`是一个 SpringBoot 的事件, 它是在上下文刷新和处理任何相关回调之后发送的，以指示应用程序已准备好为请求提供服务。

Spring boot将触发更多附加的应用程序事件。有些事件实际上是在创建ApplicationContext之前触发的，因此我们不能使用上述基于bean的侦听器接收它们。