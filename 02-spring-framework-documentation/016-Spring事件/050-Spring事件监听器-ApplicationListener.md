# 050-Spring事件监听器-ApplicationListener

[TOC]

## 一言蔽之

注册ApplicationListener有两种方法进行注册

## 注册ApplicationListener

- 通过ApplicationListener作为SpringBean注册
- 通过ConfigurableApplicationContext API 注册

### 方法一:通过ApplicationListener作为SpringBean注册

```java
public static void main(String[] args) {
  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
  // 将引导类 ApplicationListenerDemo 作为 Configuration Class
  context.register(ApplicationListenerDemo.class);

  // 方法二：基于 Spring 注解：@org.springframework.context.event.EventListener
  // b 方法：基于 ApplicationListener 注册为 Spring Bean
  // 通过 Configuration Class 来注册
  context.register(MyApplicationListener.class);
  // ApplicationEventMulticaster
  // 启动 Spring 应用上下文
  context.refresh(); // ContextRefreshedEvent
  // 启动 Spring 上下文
  context.start();  // ContextStartedEvent
  // 停止 Spring 上下文
  context.stop();  // ContextStoppedEvent
  // 关闭 Spring 应用上下文
  context.close(); // ContextClosedEvent
}
```

### 方法二:通过ConfigurableApplicationContext API 注册

```java
public static void main(String[] args) {
  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

  // 将引导类 ApplicationListenerDemo 作为 Configuration Class
  context.register(ApplicationListenerDemo.class);

  // 方法一：基于 Spring 接口：向 Spring 应用上下文注册事件
  // a 方法：基于 ConfigurableApplicationContext API 实现
  context.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
      println("ApplicationListener - 接收到 Spring 事件：" + event);
    }
  });

  // 方法二：基于 Spring 注解：@org.springframework.context.event.EventListener
  // ApplicationEventMulticaster
  // 启动 Spring 应用上下文
  context.refresh(); // ContextRefreshedEvent
  // 启动 Spring 上下文
  context.start();  // ContextStartedEvent
  // 停止 Spring 上下文
  context.stop();  // ContextStoppedEvent
  // 关闭 Spring 应用上下文
  context.close(); // ContextClosedEvent
}
```

#### 应用监听器

```java
static class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    println("MyApplicationListener - 接收到 Spring 事件：" + event);
  }
}
```

