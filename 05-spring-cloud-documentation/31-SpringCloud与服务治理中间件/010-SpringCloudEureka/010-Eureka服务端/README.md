# 010-Eureka服务端

[TOC]

## EurekaServer 简介

Eureka Server作为一个开箱即用的服务注册中心，提供了以下的功能，用以满足与Eureka Client交互的需求：

- 服务注册
- 接受服务心跳
- 服务剔除
- 服务下线
- 集群同步
- 获取注册表中服务实例信息

需要注意的是，Eureka Server同时也是一个Eureka Client，在不禁止Eureka Server的客户端行为时，它会向它配置文件中的其他Eureka Server进行拉取注册表、服务注册和发送心跳等操作。

![image-20201011191104917](../../../../assets/image-20201011191104917.png)

