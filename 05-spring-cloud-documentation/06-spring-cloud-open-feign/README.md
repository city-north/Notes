# Spring Cloud OpenFeign

这个项目主要提供了 OpenFeign 项目集成到 SpringBoot 的自动化配置以及绑定到 Spring Environment 和其他 Spring 编程模型

[OpenFeign的 github](https://github.com/OpenFeign/feign)

## 声明式 REST Client: Feign

Feign 是一个声明式web 服务客户端,目标是让 Web 服务客户端的编写更加简单,使用也非常方便,创建一个接口然后加上注解

通过可插拔的注解支持:

- Feign 注释
- JAX-RS 注释

同时 Feign 也支持 可插拔的编码和解码器.Spring Cloud 在使用`HttpMessageConverters`添加了 对 SpringMVC 的支持,同时,在使用 Feign 的时候集成了 `Ribbon` 和 `Eureka` 还有 SpringCloud `LoadBalancer` 提供了一款负载均衡的 http 客户端
