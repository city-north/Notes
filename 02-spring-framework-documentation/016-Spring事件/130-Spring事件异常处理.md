# 130-Spring事件异常处理

[TOC]

## Spring3.0错误处理接口-ErrorHandler

org.springframework.util.ErrorHandler

### 使用场景

- Spring事件(Events)
  - org.springframework.context.event.SimpleApplicationEventMulticaster 4.1开始支持
- Spring本地调度(Scheduling)
  - org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
  - org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

## SimpleApplicationEventMulticaster

org.springframework.context.event.SimpleApplicationEventMulticaster#invokeListener

```java

@Nullable
private ErrorHandler errorHandler;

protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
  ErrorHandler errorHandler = getErrorHandler();
  if (errorHandler != null) {
    try {
      doInvokeListener(listener, event);
    }
    catch (Throwable err) {
      errorHandler.handleError(err);
    }
  }
  else {
    doInvokeListener(listener, event);
  }
}
```

## 优点

如果在执行Listener过程中,没有加上ErrorHandler.

- 如果是平时使用的额时候那么有可能在超时时发生超时异常等异常,我们需要处理

- 如果是启动过程中发布的事件,出现调用Listenr失败,可能会导致整个上下文失败


## 代码演示

```java
public class AsyncEventHandlerDemo {

    public static void main(String[] args) {

        GenericApplicationContext context = new GenericApplicationContext();

        // 1.添加自定义 Spring 事件监听器
        context.addApplicationListener(new MySpringEventListener());

        // 2.启动 Spring 应用上下文
        context.refresh(); // 初始化 ApplicationEventMulticaster

        // 依赖查找 ApplicationEventMulticaster
        ApplicationEventMulticaster applicationEventMulticaster =
                context.getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);

        // 判断当前 ApplicationEventMulticaster 是否为 SimpleApplicationEventMulticaster
        if (applicationEventMulticaster instanceof SimpleApplicationEventMulticaster) {
            SimpleApplicationEventMulticaster simpleApplicationEventMulticaster =
                    (SimpleApplicationEventMulticaster) applicationEventMulticaster;
            // 切换 taskExecutor
            ExecutorService taskExecutor = newSingleThreadExecutor(new CustomizableThreadFactory("my-spring-event-thread-pool"));
            // 同步 -> 异步
            simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor);

            // 添加 ContextClosedEvent 事件处理
            applicationEventMulticaster.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {
                @Override
                public void onApplicationEvent(ContextClosedEvent event) {
                    if (!taskExecutor.isShutdown()) {
                        taskExecutor.shutdown();
                    }
                }
            });

            simpleApplicationEventMulticaster.setErrorHandler(e -> {
                System.err.println("当 Spring 事件异常时，原因：" + e.getMessage());
            });
        }

        context.addApplicationListener(new ApplicationListener<MySpringEvent>() {
            @Override
            public void onApplicationEvent(MySpringEvent event) {
                throw new RuntimeException("故意抛出异常");
            }
        });

        // 3. 发布自定义 Spring 事件
        context.publishEvent(new MySpringEvent("Hello,World"));

        // 4. 关闭 Spring 应用上下文（ContextClosedEvent）
        context.close();

    }
}

```

设置

```java
simpleApplicationEventMulticaster.setErrorHandler(e -> {
  System.err.println("当 Spring 事件异常时，原因：" + e.getMessage());
});
```

