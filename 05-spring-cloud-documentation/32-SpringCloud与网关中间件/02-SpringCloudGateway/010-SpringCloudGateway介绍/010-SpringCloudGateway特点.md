# 010-SpringCloudGateway特点

[TOC]

Spring Cloud Gateway基于Spring Boot 2，是Spring Cloud的全新项目，该项目提供了一个构建在Spring生态之上的API网关，包括Spring 5、Spring Boot 2和Project Reactor。

Spring Cloud Gateway旨在提供一种简单而有效的途径来转发请求，并为它们提供横切关注点，例如：安全性、监控/指标和弹性。

## SpringCloudGateway核心特点

Spring Cloud Gateway具有如下特征：

- 基于Java 8编码；
- 支持Spring Framework 5
- 支持Spring Boot 2
- 支持**动态路由**
- 支持内置到Spring Handler映射中的路由匹配
- 支持基于HTTP请求的路由匹配(Path、Method、Header、Host等)
- 过滤器作用于匹配的路由
- 过滤器可以修改下游HTTP请求和HTTP响应(增加/修改头部、增加/修改请求参数、改写请求路径等)
- 通过API或配置驱动
- 支持Spring Cloud DiscoveryClient配置路由，与服务发现与注册配合使用

## SpringCloudGateway和SpringCloudNetflixZuul的比较

在Finchley正式版之前，Spring Cloud推荐的网关是Netflix提供的Zuul(Zuul 1.x，是一个基于阻塞I/O的API Gateway)。

- 与Zuul相比，Spring Cloud Gateway建立在Spring Framework 5、Project Reactor和Spring Boot 2之上，使用非阻塞API。

- Spring Cloud Gateway还支持WebSocket，并且与Spring紧密集成，拥有更好的开发体验。

- Zuul基于Servlet 2.5，使用阻塞架构，它不支持任何长连接，如WebSocket。

- Zuul的设计模式和Nginx较像，每次I/O操作都是从工作线程中选择一个执行，请求线程被阻塞直到工作线程完成，但是差别是Nginx用C++实现，Zuul用Java实现，而JVM本身会有第一次加载较慢的情况，使得Zuul的性能相对较差。

- Zuul已经发布了Zuul 2.x，基于Netty、非阻塞、支持长连接，但Spring Cloud目前还没有整合。

- Zuul 2.x的性能肯定会较Zuul 1.x有较大提升。

  在性能方面，根据官方提供的基准(benchmark)测试￼，Spring Cloud Gateway的RPS(每秒请求数)是Zuul的1.6倍。综合来说，Spring Cloud Gateway在提供的功能和实际性能方面，表现都很优异。

