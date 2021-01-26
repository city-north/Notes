# 010-SpringCloud服务发现核心抽象接口

[TOC]

## 简介

SpringCloud统一了服务注册和服务发现编程模型, 其代码在spring-cloud-commons 里, 相关核心接口如下

| 接口                                                         | 作用                                   |
| ------------------------------------------------------------ | -------------------------------------- |
| org.springframework.cloud.client.discovery.DiscoveryClient   | 代表服务发现的常见读取操作             |
| org.springframework.cloud.client.discovery.EnableDiscoveryClient | 使用该注解表示开启服务发现功能         |
| org.springframework.cloud.client.discovery.ReactiveEnableDiscoveryClient | 基于响应式的代表服务发现常见的读取操作 |
| org.springframework.cloud.client.serviceregistry.ServiceRegistry | 注册与注销服务的操作封装               |
| org.springframework.cloud.client.ServiceInstance             | 代表一个服务实例                       |

统一编程模型有一下优点

- 无需关注底层服务注册/发现的实现细节, 只需要了解上层统一的抽象
- 更换注册中心非常简单, 只需要修改maven依赖和对应的注册中心配置信息(比如注册中心地址, namespace, group)

## 服务发现组件的配置

| 服务注册/服务发现组件 | maven依赖                                    | 配置项与配置值                                               |
| --------------------- | -------------------------------------------- | ------------------------------------------------------------ |
| Alibaba Nacos         | spring-cloud-starter-alibaba-nacos-discovery | spring.cloud.nacos.discovery.server-addr=localhost:8848      |
| Netflix Eureka        | spring-cloud-starter-netflix-eureka-client   | eureka.client.service-url.defaultZone=http://localhost:8761/eureka |

## 