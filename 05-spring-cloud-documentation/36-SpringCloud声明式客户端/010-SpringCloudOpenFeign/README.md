# 010-SpringCloudOpenFeign

[TOC]

## 简介

在使用 SpringCloud 开发微服务应用的时候,各个服务都是以 HTTP 接口的形式对外提供服务,因此在服务消费者调用服务提供者时,底层通过 HTTP Client 的方式调用

- JDK 原生的 URLConnection
- Apache 的 HTTP client
- Netty 的异步 HTTP client
- Spring 的 RestTemplate
- OpenFeign 客户端

SpringCloud 对 Feign 进行了增强,使得 Feign 支持 SpringMVC 的注解,并整合了 Ribbon 等,从而让 Feign 的使用更加方便

## 什么是 Feign

- 声明式 WebService 客户端, 让 web service 客户端变得更加简单
- 支持编码器解码器
- SpringCloud Feign 支持 SpringMVC 注解
- 可以做到使用 HTTP 请求访问远程服务,就像是调用本地方法一样

## Feign 的特性

- 可插拔的注解支持,包括 Feign 注解和 JAX-RS 注解
- 支持可插拔的 HTTP 编码器和解码器
- 支持 Hystrix 和它的 Fallback
- 支持 Ribbon 的负载均衡
- 支持 HTTP 请求和响应压缩

## Feign 的工作流程

- 主程序入口添加`@EnableFeignClients`注解开启对 Feign Client 扫描加载处理
- 定义接口并加`@FeignClents` 注解
- 当程序启动时,会进行包扫描,扫描所有`@FeignClients`标注的类,并将这些信息注入到 SpringIoc 容器中,当定义的 Feign 接口中的方法被调用的时候,通过 JDK 代理的方式,来生成具体的 requetTemplate 当生成代理时,Feign会为每个接口方法创建一个 RequestTemplate ,该对象封装了 HTTP 请求需要的全部信息,如请求参数名,请求方法等信息都是在这个过程中确定的
- RequestTemplate 
  - Client 可以是 JDK 的 URLConnection
  - Client 可以是 Apache  的 HttpClient
  - Client 可以是 OKhttp
- Client 被封装到 LoadBalanceClient 类,这个类结合 Ribbon 负载均衡发起服务之间的调用

## Feign和其他组件的结合

在Spring Cloud中，各个微服务一般以HTTP接口的形式暴露自身服务，因此在调用远程服务时推荐使用HTTP客户端。

OpenFeign是一种声明式、模板化的HTTP客户端。在Spring Cloud中使用OpenFeign，就可以在使用HTTP请求远程服务时获得与调用本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个HTTP请求。

Spring Cloud OpenFeign可以与

- Spring Cloud Netflix Ribbon一起使用， 实现负载均衡
- Spring Cloud Netflix Hystrix一起使用，断路器机制

## Feign的工作流程图示

![image-20200726132143323](../../../assets/image-20200726132143323.png)

