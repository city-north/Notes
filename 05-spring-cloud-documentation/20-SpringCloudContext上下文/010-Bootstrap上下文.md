# 010-Bootstrap上下文

[TOC]

## 什么是Bootstrap上下文

除了应用上下文配置

- application.yml
- application.properties之外

Spring Cloud应用程序还额外提供与Bootstrap上下文配置相关的应用属性。

## 为什么要有Bootstrap上下文

- Bootstrap上下文对于主程序来说是一个父级上下文，它支持从外部资源中加载配置文件，和解密本地外部配置文件中的属性。

- Bootstrap上下文和应用上下文将共享一个环境(Environment)，这是所有Spring应用程序的外部属性来源。一般来讲，Bootstrap上下文中的属性优先级较高，所以它们不能被本地配置所覆盖。

- Bootstrap上下文使用与主程序不同的规则来加载外部配置。因此bootstrap.yml用于为Bootstrap上下文加载外部配置，区别于应用上下文的application.yml或者application.properties。

## 开启或者禁用bootstrap上下文

```yaml
spring:
    application:
        name: my-application
    cloud:
        config:
            uri: ${CONFIG_SERVER:http://localhost:8080}
```

如果想要禁止Bootstrap引导过程，可以在bootstrap.yml中设置，如下所示：

```yaml
spring:
    cloud:
        bootstrap:
            enabled: false
```

