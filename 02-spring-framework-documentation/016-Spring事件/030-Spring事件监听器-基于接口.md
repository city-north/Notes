# 030-Spring事件监听器-基于接口

[TOC]

## Spring事件接口

- 拓展接口 : org.springframework.context.ApplicationListener
- 设计特点 : 单一类型事件处理
- 处理方法 :  void onApplicationEvent(E event);
- 事件类型 : org.springframework.context.ApplicationEvent

**单一类型事件处理**

## 源码

```java
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	/**
	 * Handle an application event.
	 * @param event the event to respond to
	 */
	void onApplicationEvent(E event);

}
```

## 代码实例

```java
public class ApplicationListenerDemo {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("接收到Spring事件" + event);
            }
        });
        applicationContext.refresh();
        applicationContext.start();
        applicationContext.stop();
        applicationContext.close();
    }
}
```

输出

```
接收到Spring事件org.springframework.context.event.ContextRefreshedEvent
接收到Spring事件org.springframework.context.event.ContextStartedEvent
接收到Spring事件org.springframework.context.event.ContextStoppedEvent
接收到Spring事件org.springframework.context.event.ContextClosedEvent
```

