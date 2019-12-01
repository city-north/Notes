# Spring Cloud Context 

Application Context Services 

Spring Boot has an opinionated view of how to build an application with Spring. For instance, it has conventional locations for common configuration files and has endpoints for common management and monitoring tasks. Spring Cloud builds on top of that and adds a few features that probably all components in a system would use or occasionally need.

- Spring Cloud 基于 Spring Boot 开发
- Spring Cloud 另外构建了一些所有组件都有可能使用到的特性

## [The Bootstrap Application Context](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#the-bootstrap-application-context)

A Spring Cloud application operates by creating a “bootstrap” context, which is a parent context for the main application. It is responsible for loading configuration properties from the external sources and for decrypting properties in the local external configuration files. The two contexts share an `Environment`, which is the source of external properties for any Spring application. By default, bootstrap properties (not `bootstrap.properties` but properties that are loaded during the bootstrap phase) are added with high precedence, so they cannot be overridden by local configuration.

- bootstrap context 是main 应用的父上下文
- 负责从外部资源加载配置文件
- 解密从本地外部化配置文件的属性
- 两个上下文共享同一个`Environment`
- Bootstrap 优先级更高

The bootstrap context uses a different convention for locating external configuration than the main application context. Instead of `application.yml` (or `.properties`), you can use `bootstrap.yml`, keeping the external configuration for bootstrap and main context nicely separate. The following listing shows an example:

```yaml
spring:
  application:
    name: foo
  cloud:
    config:
      uri: ${SPRING_CONFIG_URI:http://localhost:8888}
```

If your application needs any application-specific configuration from the server, it is a good idea to set the `spring.application.name` (in `bootstrap.yml` or `application.yml`). In order for the property `spring.application.name` to be used as the application’s context ID you must set it in `bootstrap.[properties | yml]`.

You can disable the bootstrap process completely by setting `spring.cloud.bootstrap.enabled=false` (for example, in system properties).

- 通过`bootstrap.yml`设置bootstrap 上下文
- 使用`spring.cloud.bootstrap.enabled=false`关闭 bootstrap 流程

## [ Application Context Hierarchies](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#application-context-hierarchies)

应用程序上下文层次结构

If you build an application context from `SpringApplication` or `SpringApplicationBuilder`, then the Bootstrap context is added as a parent to that context. It is a feature of Spring that child contexts inherit property sources and profiles from their parent, so the “main” application context contains additional property sources, compared to building the same context without Spring Cloud Config. The additional property sources are: