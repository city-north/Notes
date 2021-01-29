# 050-Eureka客户端-服务注册

[TOC]

## 一言蔽之

在拉取完Eureka Server中的注册表信息并将其缓存在本地后，Eureka Client将向Eureka Server注册自身服务实例元数据

使用HTTP请求

## 服务注册的发生时机

在拉取完Eureka Server中的注册表信息并将其缓存在本地后，Eureka Client将向Eureka Server注册自身服务实例元数据，主要逻辑位于DiscoveryClient#register方法中。

#### DiscoveryClient#register:注册自身服务到EurekaServer

```java
boolean register() throws Throwable {
    EurekaHttpResponse〈Void〉 httpResponse;
    try {
        httpResponse = eurekaTransport.registrationClient.register(instanceInfo);
    } catch (Exception e) {
        throw e;
    }
    ...
    // 注册成功
    return httpResponse.getStatusCode() == 204;
}
```

Eureka Client会将自身服务实例元数据(封装在InstanceInfo中)发送到Eureka Server中请求服务注册，当Eureka Server返回204状态码时，说明服务注册成功。

跟踪到AbstractJerseyEurekaHttpClient#register方法中，可以发现服务注册调用的接口以及传递的参数，如图

![image-20210128122952760](../../../../assets/image-20210128122952760.png)￼

