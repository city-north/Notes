# 040-Spring事件监听器-基于注解

[TOC]

## 核心注解

org.springframework.context.event.EventListener

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {

	@AliasFor("value")
	Class<?>[] classes() default {};

	String condition() default "";

}
```

## 注解特点

| 特征                 | 说明                                    |
| -------------------- | --------------------------------------- |
| 设计特点             | 支持多ApplicationEvent类型,无需接口约束 |
| 注解目标             | 方法                                    |
| 是否支持异步执行     | 支持,配合@Ansy注解                      |
| 是否支持泛型类型事件 | 支持,                                   |
| 是否支持顺序控制     | 支持,配合@Order注解                     |

## 代码实例

```java
@EnableAsync
public class ApplicationListenerDemo {

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

        // b 方法：基于 ApplicationListener 注册为 Spring Bean
        // 通过 Configuration Class 来注册
        context.register(MyApplicationListener.class);

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


    @EventListener
    @Order(2)//顺序
    public void onApplicationEvent(ContextRefreshedEvent event) {
        println("@EventListener(onApplicationEvent) - 接收到 Spring ContextRefreshedEvent");
    }

    @EventListener
    @Order(1)
    public void onApplicationEvent1(ContextRefreshedEvent event) {
        println("@EventListener(onApplicationEvent1) - 接收到 Spring ContextRefreshedEvent");
    }

    @EventListener
    @Async // 异步实现
    public void onApplicationEventAsync(ContextRefreshedEvent event) {
        println("@EventListener（异步） - 接收到 Spring ContextRefreshedEvent");
    }

    @EventListener
    public void onApplicationEvent(ContextStartedEvent event) {
        println("@EventListener - 接收到 Spring ContextStartedEvent");
    }

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event) {
        println("@EventListener - 接收到 Spring ContextClosedEvent");
    }

    private static void println(Object printable) {
        System.out.printf("[线程：%s] : %s\n", Thread.currentThread().getName(), printable);
    }
```

