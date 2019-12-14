# Spring Cloud Gateway

版本: **Hoxton.RELEASE**

> This project provides an API Gateway built on top of the Spring Ecosystem, including: Spring 5, Spring Boot 2 and Project Reactor. Spring Cloud Gateway aims to provide a simple, yet effective way to route to APIs and provide cross cutting concerns to them such as: security, monitoring/metrics, and resiliency.

- Spring Cloud Gateway 提供了一个 API 网关
- 使用 Spring 生态圈的技术有
  -  Spring 5
  - Spring Boot 2 
  -  Project Reactor
- 提供了一个高效的方式去路由 API
- 提供了通用的关注点处理,如安全,监控,指标和弹性

# 引入 SpringCloud Gateway

> - **本文作者：**二当家的
> - **本文链接：** [2018/12/25/spring cloud gateway系列教程1——Route Predicate/](https://www.edjdhbb.com/2018/12/25/spring cloud gateway系列教程1——Route Predicate/)
> - **版权声明：** 本博客所有文章除特别声明外，均采用 [CC BY-NC-SA 3.0 CN](http://creativecommons.org/licenses/by-nc-sa/3.0/cn/) 许可协议。转载请注明出处！

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

**Spring Cloud Gateway使用的是Spring Boot和Spring Webflux提供的Netty底层环境，不能和传统的Servlet容器一起使用，也不能打包成一个WAR包。**

## 术语表

> - **Route**: Route the basic building block of the gateway. It is defined by an ID, a destination URI, a collection of predicates and a collection of filters. A route is matched if aggregate predicate is true.
>
> - **Predicate**: This is a [Java 8 Function Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html). The input type is a [Spring Framework `ServerWebExchange`](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/server/ServerWebExchange.html). This allows developers to match on anything from the HTTP request, such as headers or parameters.
>
> - **Filter**: These are instances [Spring Framework `GatewayFilter`](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/server/GatewayFilter.html) constructed in with a specific factory. Here, requests and responses can be modified before or after sending the downstream request.
>
> 

- 路由:路由是网关的基本构件,它由ID、目标URI、谓词集合和筛选器集合定义。如果聚合谓词为真，则匹配路由。

- **Predicate**:[Java 8 Function Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html),允许开发者匹配任何 Http 请求,例如请求 header

- 过滤器: [Spring Framework `GatewayFilter`](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/server/GatewayFilter.html)的实例,在这里,请求在发送给下游请求之前对请求进行修改

## 工作原理

![image-20191212160045947](assets/image-20191212160045947.png)

![image-20191212160114944](assets/image-20191212160114944.png)

- 客户端发送请求到 springCloud gataway
- 如果 gateway handler mapping 映射能够匹配这个请求到一个路由,就会发送到 Gatway Web Handler
- Gatway Web Handler 发送这请求到 fitler 链(过滤器被虚线分隔的原因是，过滤器可能在发送代理请求之前或之后执行逻辑。)
- 执行所有“pre”筛选器逻辑，然后发出代理请求。发出代理请求后，将执行“post”筛选器逻辑。
- 发送到指定路由

## 