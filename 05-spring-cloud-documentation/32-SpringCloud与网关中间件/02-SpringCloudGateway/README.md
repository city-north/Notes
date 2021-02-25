# 02-SpringCloudGateway

[TOC]



SpringCloud Gateway 基于

- Spring 5.0
- SpringBoot 2.0
- Project Reactor 

开发, 主要目的是提供简单,有效且统一的 API 路由管理方式,其目标是代替 NetFlix Zuul , 

主要提供了

- 统一的路由方式

- 基于 Filter 链的方式提供了网关基本的功能, 例如

  - 协议适配
  - 协议转发
  - 安全策略
  - 防刷
  - 流量,监控日志

  等等

## 是什么

- 分布式系统网关

> - 协议适配
> - 协议转发
> - 安全策略(WAF)
> - 防刷
> - 流量
> - 监控日志

一般来说,网关提供 API 全托管服务,丰富的 API管理功能,辅助企业管理大规模 API ,以降低成本和安全风险

网关对外暴露 URL 或者接口信息 , 统称为路由信息

## 怎么工作的

![Spring Cloud Gateway Diagram](../../../assets/spring_cloud_gateway_diagram.png)

- 客户端发送请求到 gateway

- 首先会被 HttpwebHandlerAdapter 进行提取组装成网关的上下文

- 网关上下文会传递到 DispatcherHandler ,  DispatcherHandler 是所有请求的分发处理器

  > 主要负责分发请求对应的处理器,比如将请求分发到对应的 RoutePredicateHandlerMapping 路由断言处理映射器

- 路由断言处理映射器主要用于**路由**的查找,以及找到路由后返回对应的 FilteringWebHandler 

- FilteringWebHandler 主要负责组装 Filter链表并调用 Filter 执行一系列 Filter 操作,然后把请求转到后端对应的代理服务器处理

- 处理完成之后,将 Response 返回到 Gateway 客户端



<img src="../../../assets/image-20200615123602357.png" alt="image-20200615123602357" style="zoom:50%;" />

值得注意的是

在 Filter 链中,通过虚线分隔 filter 的原因是, 过滤器可以在转发请求之前处理或者接收到被代理对象的返回结果之后处理

所有的 pre 类型的 Filter 执行完毕之后,才会转发请求到被代理的服务处理

被代理的服务把所有请求处理完毕之后, 才会执行 Post 类型的过滤器

> 先走断言,断言成功了才走 filter

## 核心概念

![image-20191212160045947](../../../assets/image-20191212160045947.png)

#### 路由(route)

路由是网关最基础的部分,路由信息组成

- 一个 ID

- 一个目的 URL

- 一组断言工厂

  > 如果断言为真,则说明请求的 url 和配置的路由匹配

- 一组 Filter

#### 断言 Predicate

Java8 中的断言函数, SC gatway 项目中的断言函数输入类型是 Spring5.0框架中的 `ServerWebExchange` 

> 有了断言,我们可以断言 Http Request 中的任何信息,比如请求头和参数

#### 过滤器 Filter 

一个标准的 Spring WebFilter , SC Gatway 中的 filter 分为两类

- Gateway FIlter

> Gateway Filter 是从 web Filter 复制过来的,相当于一个 Filter 过滤器,可以对访问的 URL过滤,进行横切处理(切面处理) ,应用场景包括超时,安全等

- Global Filter

> Spring Cloud Gateway 定义了 GlobalFIlter 接口,让我们可以自定义实现自己的 Global FIlter  , Global Filter 是一个全局 Filter ,作用于所有路由



## 如何使用

## 添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

引入依赖后的开关,默认是开启的

```properties
spring.cloud.gateway.enabled=false
```

不要引入`spring-boot-starter-web` 会有冲突

**Spring Cloud Gateway使用的是Spring Boot和`Spring Webflux`提供的Netty底层环境，不能和传统的Servlet容器一起使用，也不能打包成一个WAR包。**





