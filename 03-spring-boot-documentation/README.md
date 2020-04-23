# SpringBoot

## Introducing Spring Boot

> Spring Boot makes it easy to create stand-alone, production-grade Spring-based Applications that you can run. We take an opinionated view of the Spring platform and third-party libraries, so that you can get started with minimum fuss. Most Spring Boot applications need very little Spring configuration.
>
> You can use Spring Boot to create Java applications that can be started by using `java -jar` or more traditional war deployments. We also provide a command line tool that runs “spring scripts”.

- Spring Boot 用于创建独立部署,生产级别的 Spring 应用
- 你可以用 Spring 创建一个使用`java -jar`运行的 jar 包应用或者是传统 war 包部署
- 提供了一个控制台工具来运行 "spring scripts"

> Our primary goals are:
>
> - Provide a radically faster and widely accessible getting-started experience for all Spring development.
> - Be opinionated out of the box but get out of the way quickly as requirements start to diverge from the defaults.
> - Provide a range of non-functional features that are common to large classes of projects (such as embedded servers, security, metrics, health checks, and externalized configuration).
> - Absolutely no code generation and no requirement for XML configuration.

主要目标

- 为所有Spring开发提供更快、更广泛的启动体验。
- 提倡开箱即用,但是当需求偏离默认值时能快速完成实现
- 提供对大型项目类(如嵌入式服务器、安全性、度量标准、健康检查和外部化配置)通用的一系列非功能特性。
- 完全不需要代码生成，也不需要XML配置。

## System Requirements

Spring Boot 2.2.1.RELEASE requires [Java 8](https://www.java.com/) and is compatible up to Java 13 (included). [Spring Framework 5.2.1.RELEASE](https://docs.spring.io/spring/docs/5.2.1.RELEASE/spring-framework-reference/) or above is also required.

Explicit build support is provided for the following build tools:

| Build Tool | Version                                               |
| :--------- | :---------------------------------------------------- |
| Maven      | 3.3+                                                  |
| Gradle     | 5.x (4.10 is also supported but in a deprecated form) |

## Servlet Containers

Spring Boot supports the following embedded servlet containers:

| Name         | Servlet Version |
| :----------- | :-------------- |
| Tomcat 9.0   | 4.0             |
| Jetty 9.4    | 3.1             |
| Undertow 2.0 | 4.0             |

You can also deploy Spring Boot applications to any Servlet 3.1+ compatible container.

## SpringBoot 的特性

- 创建独立的 Spring 应用
- 直接嵌入 tomcat, Jetty, Undertow 等 web 容器(不需要部署 war 文件)
- 提供固话的"starter"依赖,简化构建配置
- 当条件满足的时候,自动地装配 Spring 或者第三方库
- 提供运维(production-ready) 的特性, 例如 指标信息(Metrics),健康检查以及外部化配置
- 绝无代码生成,不需要 xml 配置