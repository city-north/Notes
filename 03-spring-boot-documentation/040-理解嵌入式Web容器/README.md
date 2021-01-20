# 040-理解嵌入式Web容器

早在Tomcat5.x和Jetty 5.x 就已经支持嵌入式容器, Serlvet规范实现与这三种Servlet容器的版本对应的关系如下

| Servlet规范 | Tomcat | Jetty | Undertow |
| ----------- | ------ | ----- | -------- |
| 4.0         | 9.x    | 9.x   | 2.x      |
| 3.1         | 8.x    | 8.x   | 1.x      |
| 3.0         | 7.x    | 7.x   | N/A      |
| 2.5         | 6.x    | 6.x   | N/A      |

## 嵌入式ReactiveWeb

嵌入式ReactiveWeb容器,,默认实现为 Netty Web Server ,与业界其他基于Netty实现的web Server 类似, 例如 Eclipse vert.x

Spring WebFlux 基于 Reactor 框架实现, 因此在SpringBoot 引入,通常被 spring-boot-starter-webflux简介引入

一下三种服务器都可以作为Reactvie web server ,是因为 Servlet 3.1 +  容器同样满足 Reactive 异步非阻塞的特性

- Tomcat
- Jetty
- Undertow

所以热门的Web容器实现均支持嵌入式容器方式,这并非SpringBoot的独创,而是整合了技术

