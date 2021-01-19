# SELF PERSERVATION 自我保护机制

[TOC]

## 是什么

Eureka Client 端和 Eureka Server 端之间有一个租约,Server 端会有定时任务剔除失效的实例

- Client 定时发送心跳包维持这个租约
- Server 通过当前注册的实例数,计算出每分钟应该受到多少心跳包,如果最近一分钟接收到的续租次数小于等于指定阈值的话,**就关闭租约失效剔除**,禁止定时任务剔除失效的实例,从而保护注册信息

## 为什么要有这个机制

首先是注册中心都有健康检查,比较关键的问题是,处理好网络偶尔波动或者短暂不可用造成的误判

> 当我们在断点调试的时候,通常就会造成一种短暂不可用的情况

如果出现网络分区的问题,极端情况下,Eureka Server 情况部分服务的实例列表,将会严重影响到 EurekaServer 的 Availability 属性,因此要有一个机制,禁止它这种极端剔除服务的行为,从而保护注册信息

## 附录

https://medium.com/knerd/eureka-why-you-shouldnt-use-zookeeper-for-service-discovery-4932c5c7e764

