# 060-Spring事件监听发布器-ApplicationEventPublisher

[TOC]

## 一言蔽之

ApplicationEventPublisher 是事件发布器, AbstractApplicationContext 本身就是一个 ApplicationEventPublisher  ,底层委派给ApplicationEventMuticaster实现

## 事件发布的方法

- 通过ApplicationEventPublisher发布
  - 依赖注入
- 通过ApplicationEventMulticaster发布
  - 依赖注入
  - 依赖查找

## ApplicationEventPublisher源码

```java
@FunctionalInterface
public interface ApplicationEventPublisher {

	default void publishEvent(ApplicationEvent event) {
		publishEvent((Object) event);
	}
  
	//PayloadApplicationEvent 实现
	void publishEvent(Object event);
}
```

## 获取方式

- ApplicationEventPublisherAware

```java
public class ApplicationListenerDemo implements ApplicationEventPublisherAware {

		@Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        applicationEventPublisher.publishEvent(new ApplicationEvent("Hello,World") {
        });

        // 发送 PayloadApplicationEvent
        applicationEventPublisher.publishEvent("Hello,World");
        applicationEventPublisher.publishEvent(new MyPayloadApplicationEvent(this, "Hello,World"));
    }
  
  @EventListener
  public void onPayloadApplicationEvent(PayloadApplicationEvent<String> event) {
    println("onPayloadApplicationEvent - 接收到 Spring PayloadApplicationEvent：" + event);
  }

}
```

## 底层实现

在底层实现,委派给了ApplicationEventMuticaster

 [065-Spring事件监听广播器-ApplicationEventMuticaster.md](065-Spring事件监听广播器-ApplicationEventMuticaster.md) 

```java
    protected void publishEvent(Object event, ResolvableType eventType) {
        Assert.notNull(event, "Event must not be null");
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Publishing event in " + this.getDisplayName() + ": " + event);
        }

        Object applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent)event;
        } else {
            applicationEvent = new PayloadApplicationEvent(this, event);
            if (eventType == null) {
                eventType = ((PayloadApplicationEvent)applicationEvent).getResolvableType();
            }
        }

        if (this.earlyApplicationEvents != null) {
            this.earlyApplicationEvents.add(applicationEvent);
        } else {
          //委派
            this.getApplicationEventMulticaster().multicastEvent((ApplicationEvent)applicationEvent, eventType);
        }

        if (this.parent != null) {
            if (this.parent instanceof AbstractApplicationContext) {
                ((AbstractApplicationContext)this.parent).publishEvent(event, eventType);
            } else {
                this.parent.publishEvent(event);
            }
        }

    }
```

