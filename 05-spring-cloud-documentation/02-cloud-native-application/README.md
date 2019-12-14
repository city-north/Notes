# Cloud Native Applications

云原生应用

> [Cloud Native](https://pivotal.io/platform-as-a-service/migrating-to-cloud-native-application-architectures-ebook) is a style of application development that encourages easy adoption of best practices in the areas of continuous delivery and value-driven development. A related discipline is that of building [12-factor Applications](https://12factor.net/), in which development practices are aligned with delivery and operations goals — for instance, by using declarative programming and management and monitoring. Spring Cloud facilitates these styles of development in a number of specific ways. The starting point is a set of features to which all components in a distributed system need easy access.

- Cloud Native 是一种鼓励在持续交付和价值驱动开发领域轻松采用最佳实践的应用程序开发风格。
- 一个相关的原则是构建 [12-factor](../01-basic/04-12-factor.md)，在这个原则中，开发实践与交付和操作目标是一致的
- SpringCloud 使用不同的方式实践着这个风格

> Many of those features are covered by [Spring Boot](https://projects.spring.io/spring-boot), on which Spring Cloud builds. Some more features are delivered by Spring Cloud as two libraries: Spring Cloud Context and Spring Cloud Commons. Spring Cloud Context provides utilities and special services for the `ApplicationContext` of a Spring Cloud application (bootstrap context, encryption, refresh scope, and environment endpoints). Spring Cloud Commons is a set of abstractions and common classes used in different Spring Cloud implementations (such as Spring Cloud Netflix and Spring Cloud Consul).

- SpringBoot 覆盖了这些特性,SpringCloud 基于 SpringBoot 构建

- 更多的特性体现在 SpringCloud 的两个库中

  - Spring Cloud Context

    提供了基于`ApllicationContet`的工具类与特殊服务如

    - bootstrap context
    - encryption
    - refresh scope
    - environment endpoints

  - Spring Cloud Commons

  是一组SpringCloud 不同实现类的抽象

