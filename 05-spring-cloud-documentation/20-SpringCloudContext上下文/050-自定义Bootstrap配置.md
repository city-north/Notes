# 050-自定义Bootstrap配置

[TOC]

## 自定义Bootstrap配置过程

自定义Bootstrap配置过程与Spring Boot自动配置运行原理类似，

- 具体操作是在/META-INF/spring.factories文件中添加org.springframework.cloud.bootstrap.BootstrapConiguration配置项。

配置项的值是一系列用来创建Context的@Configuration配置类，配置类之间以逗号分隔。这些类可以为应用上下文提供Bean实例，配置类可以通过标记@Order来控制Bean实例初始化序列。如下例所示，在引导过程中添加了一个LogAutoConfiguration的配置类，为应用程序添加日志相关的Bean实例：

```xml
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
    com.demo.starter.config.LogAutoConfiguration
```





