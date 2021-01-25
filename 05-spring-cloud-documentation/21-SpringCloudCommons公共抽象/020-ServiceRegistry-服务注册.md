# 020-ServiceRegistry-服务注册

[TOC]

## 什么是ServiceRegistry

Spring Cloud Commons的ServiceRegister接口提供register(服务注册)和deregister(服务下线)方法，使开发者可以自定义注册服务的逻辑，如下所示：

```java
public interface ServiceRegistry〈R extends Registration〉 {
    void register(R registration);
		void deregister(R registration);
}
```

每一个ServiceRegistry的实现都拥有自己的注册表实现，如Eureka、Consul等。
3.