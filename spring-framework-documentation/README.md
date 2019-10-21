# Spring 官方文档学习笔记

[Spring Framework Reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/)

## 学习整体思路

在修理冰箱之前,一定是先看冰箱说明书,同样,在学习 Spring 源码前,一定要看 Spring 官方文档

- 知识点穿插使用的案例
- 分析 Spring 做法是否合适

## Spring 简介

Spring makes it easy to create Java enterprise applications. It provides everything you need to embrace the Java language in an enterprise environment, with support for Groovy and Kotlin as alternative languages on the JVM, and with the flexibility to create many kinds of architectures depending on an application’s needs. As of Spring Framework 5.1, Spring requires JDK 8+ (Java SE 8+) and provides out-of-the-box support for JDK 11 LTS. Java SE 8 update 60 is suggested as the minimum patch release for Java 8, but it is generally recommended to use a recent patch release.

- Spring 致力于让 Java 企业级应用的构建更加简单
- 支持 Groovy 和 Kotlin
- Spring 5.1 以上 版本需要 JDK8+,支持 JDK 11 LTS
- 建议最低 Java8 update 60 以上版本

Spring supports a wide range of application scenarios. In a large enterprise, applications often exist for a long time and have to run on a JDK and application server whose upgrade cycle is beyond developer control. Others may run as a single jar with the server embedded, possibly in a cloud environment. Yet others may be standalone applications (such as batch or integration workloads) that do not need a server.

Spring 支持广泛的应用场景:

- 在大型企业级软件中,应用往往存在很长时间.必须在 JDK 和应用服务器上运行,而他们的升级周期对于开发人员来说,不可控
- 又有一些应用作为单个 jar 文件内嵌到服务器中执行
- 也有一些应用作为独立的应用(例如批处理或集成工作负载),不需要一个服务器

Spring is open source. It has a large and active community that provides continuous feedback based on a diverse range of real-world use cases. This has helped Spring to successfully evolve over a very long time.

- 开源
- 社区活跃
- 基于生产环境实例的反馈

