# 050-SpringCloud服务注册抽象-ServiceRegistry

[TOC]

## 一言蔽之

SpringCloud 的ServiceRegistry是对服务注册的抽象

```
org.springframework.cloud.client.serviceregistry.ServiceRegistry
```

## 服务注册和销毁的过程

- `AutoServiceRegistration`是一个标记接口, 没有任何方法
- 大部分的实现在`AbstractAutoServiceRegistration`中,同时也实现了`ApplicationListener<WebServerInitializedEvent>`用来接受事件
  - 收到WebServerInitializedEvent 事件时, 使用 ServiceRegistry完成了服务注册
  - 应用关闭时会触发 @PreDestory注解使用 ServiceRegistry完成服务的销毁

![image-20210128204606277](../../../assets/image-20210128204606277.png)

## 接口定义

```java
public interface ServiceRegistry<R extends Registration> {
	//基于实例信息将其注册到注册中心
	void register(R registration);
	//基于实例信息将其从注册中心注销
	void deregister(R registration);
	// 关闭ServiceRegistry
	void close();
	// 设置服务状态
	void setStatus(R registration, String status);
	//获取状态
	<T> T getStatus(R registration);
}
```

