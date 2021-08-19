# 010-Spring统一消息编程模型

[TOC]

## 为什么要统一

无论是Apache RacketMQ 的 Message 还是 Apache Kafka的 ProducerRecord 在 spring-messaging中统一被称为 Message

```
org.springframework.messaging.Message 接口
```

## 两个方法

```java
public interface Message<T> {

	/**
	 * Return the message payload.
	 */
	T getPayload();

	/**
	 * Return message headers for the message (never {@code null} but may be empty).
	 */
	MessageHeaders getHeaders();

}

```

- `MessageHeaders` 是  一个实现了Map接口的类,
- MessageHeader 内部对数据做了一些限制,  是一个 Immutable类型的对象, 不能随意对其中的元数据进行修改, 以k-v的形式的消息头可以设置任意的value