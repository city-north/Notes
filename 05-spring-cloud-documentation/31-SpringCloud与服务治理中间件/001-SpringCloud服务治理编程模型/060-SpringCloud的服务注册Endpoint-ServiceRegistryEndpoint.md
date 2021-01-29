# 060-SpringCloud的服务注册Endpoint-ServiceRegistryEndpoint

[TOC]

## 一言蔽之



## 什么是ServiceRegistryEndpoint

ServiceRegistryEndpoint 是 SpringCloud 注册/发现功能对外暴露的EndPoint ,其ID是 service-registery ,用于获取和设置服务实例的状态

- 获取和设置的动作由ServiceResgistry的getStatus 和 setStatus

## 引入流程

- 添加 spring-boot-starter-actuator 依赖
- 配置 

```
spring.autoconfigure.exclude=org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration,org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration
```

## ServiceRegistryEndpoint的作用

- 无损下线注册中心

为什么要无损下线呢, 应用的无损下线, 比如 某个应用有10个实例, 

- 某天需要发布新版本, 发布新版意味着需要先下线部分实例(下线旧的版本)
- 然后使用新的部署包发布新的版本(类似于滚动上线)

SpringCloud注册中心默认30s去注册中心获取服务对应的实例列表来覆盖内存里的实例信息

- 如果一些实例下线了,但是调用这个服务的客户端还没有30s刷新, 那么客户端就会报异常

## 无损下线思路

1. 调用ServiceRegistryEndpoint, 将需要下线的实例的服务下线()
2. 服务细线后,等待客户端刷新30s, 通过刷新实例列表来删除已下线的实例(这个时候只是服务下线, 实例并未下线)
3. 实例下线(服务发现已经不能添加实例,就可以安全的下线实例)

#### 下线命令

```http
curl --header "Content-Type:application/json" -XPOST http://host:port/actuator/service-registry -d '{"status" : "DOWN"}'
```

