# 020-Spring标准事件-ApplicationEvent

[TOC]

## 一言蔽之

Spring标准事件ApplicationEvent 拓展自Java标准事件EventObject, 在此基础上添加了发生的事件, 所有Spring的事件都继承自ApplicationEvent 

## Spring事件核心组件API

| 类名称           | 核心类                                                       |
| ---------------- | ------------------------------------------------------------ |
| Spring事件       | org.springframework.context.ApplicationEvent                 |
| Spring事件监听器 | org.springframework.context.ApplicationListener              |
| Spring事件发布器 | org.springframework.context.ApplicationEventPublisher        |
| Spring事件广播器 | org.springframework.context.event.ApplicationEventMulticaster |

## UML

![image-20210107181245453](../../assets/image-20210107181245453.png)

## Spring事件特性-ApplicationEvent

ApplicationEvent 在 EventObject 的基础上 , 拓展了事件发生的时间 timestamp

```java
public abstract class ApplicationEvent extends EventObject {

	/** use serialVersionUID from Spring 1.2 for interoperability */
	private static final long serialVersionUID = 7099057708183571937L;

	/** System time when the event happened */ // 时间戳
	private final long timestamp;

	public ApplicationEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}
}

```

## Spring应用上下文拓展ApplicationContextEvent

具体的实现由四个

- org.springframework.context.event.ContextRefreshedEvent : Spring应用上下文就绪事件
- org.springframework.context.event.ContextStartedEvent : Spring应用上下文启动事件
- org.springframework.context.event.ContextStoppedEvent :  Spring上下文停止事件
- org.springframework.context.event.ContextClosedEvent :  Spring上下文关闭事件

#### Spring应用上下文就绪事件-org.springframework.context.event.ContextRefreshedEvent

ContextRefreshedEvent的事件源是ApplicationContext

```java
public class ContextRefreshedEvent extends ApplicationContextEvent {
	public ContextRefreshedEvent(ApplicationContext source) {
		super(source);
	}
}
```

发布时机

```java
org.springframework.context.support.AbstractApplicationContext#finishRefresh
```

 [110-Spring应用上下刷新完成阶段-finishRefresh.md](../019-Spring应用上下文生命周期/110-Spring应用上下刷新完成阶段-finishRefresh.md) 

```java
	protected void finishRefresh() {
		// Clear context-level resource caches (such as ASM metadata from scanning).
		clearResourceCaches();

		// Initialize lifecycle processor for this context.
		initLifecycleProcessor();

		// Propagate refresh to lifecycle processor first.
		getLifecycleProcessor().onRefresh();

		// Publish the final event.
    //发送就绪事件
		publishEvent(new ContextRefreshedEvent(this));

		// Participate in LiveBeansView MBean, if active.
		LiveBeansView.registerApplicationContext(this);
	}

```

发布源码

```java
	protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
		Assert.notNull(event, "Event must not be null");

		// Decorate event as an ApplicationEvent if necessary
		ApplicationEvent applicationEvent;
		if (event instanceof ApplicationEvent) {
			applicationEvent = (ApplicationEvent) event;
		}
		else {
			applicationEvent = new PayloadApplicationEvent<>(this, event);
			if (eventType == null) {
				eventType = ((PayloadApplicationEvent) applicationEvent).getResolvableType();
			}
		}

		// Multicast right now if possible - or lazily once the multicaster is initialized
		if (this.earlyApplicationEvents != null) {
			this.earlyApplicationEvents.add(applicationEvent);
		}
		else {
			getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
		}

		// Publish event via parent context as well...
		if (this.parent != null) {
			if (this.parent instanceof AbstractApplicationContext) {
				((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
			}
			else {
				this.parent.publishEvent(event);
			}
		}
	}
```

## earlyApplicationEvent早期事件

```java
@Override
public void refresh() 
  prepareRefresh();
	initApplicationEventMulticaster();
	onRefresh();
	registerListeners();
	finishBeanFactoryInitialization(beanFactory);
	finishRefresh();
}

```

在refreh方法中,先注册的广播器,后注册的监听器

- 如果在监听器没有注册之前,就触发了发送,那么会导致消息无法正常接收
- 使用earlyApplicationEvent存储下来,到监听器都初始化好以后再发送

#### Spring应用上下文启动事件:org.springframework.context.event.ContextStartedEvent

发布时机

```java
@Override
public void start() {
  getLifecycleProcessor().start();
  publishEvent(new ContextStartedEvent(this));
}
```

#### Spring上下文停止事件:org.springframework.context.event.ContextStoppedEvent

发布时机

```java
@Override
public void stop() {
  getLifecycleProcessor().stop();
  publishEvent(new ContextStoppedEvent(this));
}
```

#### Spring上下文关闭事件-org.springframework.context.event.ContextClosedEvent 

org.springframework.context.support.AbstractApplicationContext#doClose

```java
	protected void doClose() {
		if (this.active.get() && this.closed.compareAndSet(false, true)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing " + this);
			}

			LiveBeansView.unregisterApplicationContext(this);

			try {
				// Publish shutdown event.
				publishEvent(new ContextClosedEvent(this));
        
        //.
```

