# 110-自定义Spring事件

[TOC]

## 自定义步骤

1. 拓展 org.springframework.context.ApplicationEvent
2. 实现 org.springframework.context.ApplicationListener
3. 注册 org.springframework.context.ApplicationListener

### 拓展 org.springframework.context.ApplicationEvent

```java
public class MySpringEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param message 事件消息
     */
    public MySpringEvent(String message) {
        super(message);
    }

    @Override
    public String getSource() {
        return (String) super.getSource();
    }

    public String getMessage() {
        return getSource();
    }
}

```

### 实现 org.springframework.context.ApplicationListener

```java
public class MySpringEventListener implements ApplicationListener<MySpringEvent> {

    @Override
    public void onApplicationEvent(MySpringEvent event) {
        System.out.printf("[线程 ： %s] 监听到事件 : %s\n", Thread.currentThread().getName(), event);
    }
}

```

### 注册 org.springframework.context.ApplicationListener

```java
public class CustomizedSpringEventDemo {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        // 1.添加自定义 Spring 事件监听器
        // ListenerRetriever -> 0 .. N 个 ApplicationListener<MySpringEvent> 实例
        // MySpringEvent 以及它子孙类
        context.addApplicationListener(new MySpringEventListener());

        context.addApplicationListener(new ApplicationListener<ApplicationEvent>() {

            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("Event : " + event);
            }
        });

        // 2.启动 Spring 应用上下文
        context.refresh();

        // 3. 发布自定义 Spring 事件
        // ListenerCacheKey -> MySpringEvent
        context.publishEvent(new MySpringEvent("Hello,World"));
        context.publishEvent(new MySpringEvent2("2020"));

        // 4. 关闭 Spring 应用上下文
        context.close();
    }
}
```

