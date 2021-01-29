# 010-Eureka客户端工作基本流程

[TOC]

## Eureka客户端生命周期

Eureka Client为了简化开发人员的开发工作，将很多与Eureka Server交互的工作隐藏起来，自主完成。在应用的不同运行阶段在后台完成工作如图

![image-20201011192146029](../../../../assets/image-20201011192146029.png)

Eureka客户端的生命周期分为三个阶段

- 应用启动阶段
- 应用执行阶段
- 应用销毁阶段

## 应用启动阶段

1. 读取与Eureka Server交互的配置信息, 封装成 EurekaClientConfig
2. 读取自身服务实例的配置信息, 封装成 EurekaInstanceConfig
3. 从Eureka Server中拉取注册表并缓存到本地
4. 注册服务
5. 初始化心跳机制
6. 初始化缓存刷新机制(从注册表信息更新到本地缓存)
7. 初始化按需注册定时任务(监控服务实例信息变化, 决定是否重新发起注册表中的服务实例元数据)

## EurekaClient 初始化相关核心类

- 自动化配置 : org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration
- Eureka Client自动配置类 : org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration
- Ribbon负载均衡相关配置 :  org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration

## 读取与Eureka Server交互的配置信息, 封装成 EurekaClientConfig

 [020-Eureka客户端如何读取配置信息.md](020-Eureka客户端如何读取配置信息.md) 

## 初始化按需注册定时任务(监控服务实例信息变化, 决定是否重新发起注册表中的服务实例元数据)

