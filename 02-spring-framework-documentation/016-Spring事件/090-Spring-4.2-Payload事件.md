# 090-Spring-4.2-Payload事件

[TOC]

## 使用场景

简化Spring事件发送,关注事件源主体

## 源码

```java
public class PayloadApplicationEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

	private final T payload;


	/**
	 * Create a new PayloadApplicationEvent.
	 * @param source the object on which the event initially occurred (never {@code null})
	 * @param payload the payload object (never {@code null})
	 */
	public PayloadApplicationEvent(Object source, T payload) {
		super(source);
		Assert.notNull(payload, "Payload must not be null");
		this.payload = payload;
	}


	@Override
	public ResolvableType getResolvableType() {
    //获取泛型类型
		return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getPayload()));
	}

	/**
	 * Return the payload of the event.
	 */
	public T getPayload() {
		return this.payload;
	}

}

```

## 发送方法

org.springframework.context.ApplicationEventPublisher#publishEvent(java.lang.Object)

```java
@FunctionalInterface
public interface ApplicationEventPublisher {


	default void publishEvent(ApplicationEvent event) {
		publishEvent((Object) event);
	}

	/**
	 * Notify all <strong>matching</strong> listeners registered with this
	 * application of an event.
	 * <p>If the specified {@code event} is not an {@link ApplicationEvent},
	 * it is wrapped in a {@link PayloadApplicationEvent}.
	 * <p>Such an event publication step is effectively a hand-off to the
	 * multicaster and does not imply synchronous/asynchronous execution
	 * or even immediate execution at all. Event listeners are encouraged
	 * to be as efficient as possible, individually using asynchronous
	 * execution for longer-running and potentially blocking operations.
	 * @param event the event to publish
	 * @since 4.2
	 * @see #publishEvent(ApplicationEvent)
	 * @see PayloadApplicationEvent
	 */
  //专门发送PayLoad
	void publishEvent(Object event);

}

```

