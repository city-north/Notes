# 120-Spring应用上下文启动阶段-start()

[TOC]

## Spring应用上下文启动阶段做了什么?

- AbstractApplicationContext#start()方法
  - 启动LifecycleProcessor
    - 依赖查找 Lifecycle Beans
    - 启动 Lifecyle Beans
  - 发布Spring应用上下文已启动事件-ContextStaredEvent

## 源码

要主动调用才能够刷新

```java
@Override
public void start() {
  getLifecycleProcessor().start();
  publishEvent(new ContextStartedEvent(this));
}
```

#### 启动LifecycleProcessor

启动LifecycleProcssor提供的回调

```java
LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
  if (this.lifecycleProcessor == null) {
  throw new IllegalStateException("LifecycleProcessor not initialized - " +
             "call 'refresh' before invoking lifecycle methods via the context: " + this);
  }
  return this.lifecycleProcessor;
}
```

#### 发布Spring应用上下文已启动事件-ContextStaredEvent

```java
publishEvent(new ContextStartedEvent(this));
```

