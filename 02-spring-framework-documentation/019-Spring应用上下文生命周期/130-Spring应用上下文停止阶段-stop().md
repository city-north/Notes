# 130-Spring应用上下文停止阶段-stop()

[TOC]

## Spring应用上下文停止阶段做了什么?

- AbstractApplication#stop()方法
  - 停止LifecycleProcessor
    - 依赖查找lifecycle Beans
    - 停止 Lifecycle Beans
  - 发布 Spring应用上下已停止事件- ContextStoppedEvent

## 源码

主动调用org.springframework.context.Lifecycle#stop

```
@Override
public void stop() {
   getLifecycleProcessor().stop();
   publishEvent(new ContextStoppedEvent(this));
}
```

## DEMO

```java
public class LifecycleDemo {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        // 注解 MyLifecycle 成为一个 Spring Bean
        context.registerBeanDefinition("myLifecycle", rootBeanDefinition(MyLifecycle.class).getBeanDefinition());

        // 刷新 Spring 应用上下文
        context.refresh();

        // 启动 Spring 应用上下文
        context.start();

        // 停止 Spring 应用上下文
        context.stop();

        // 关闭 Spring 应用
        context.close();
    }
}

```

```java
public class MyLifecycle implements Lifecycle {

    private boolean running = false;

    @Override
    public void start() {
        running = true;
        System.out.println("MyLifecycle 启动...");
    }

    @Override
    public void stop() {
        running = false;
        System.out.println("MyLifecycle 停止...");
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
```

MyLifecycle 启动...
MyLifecycle 停止...