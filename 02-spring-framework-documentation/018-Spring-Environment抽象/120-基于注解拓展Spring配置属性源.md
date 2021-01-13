# 120-基于注解拓展Spring配置属性源

[TOC]

## org.springframework.context.annotation.PropertySource实现原理

- 入口 - org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass
- org.springframework.context.annotation.ConfigurationClassParser#processPropertySource

4.3新增语义

- 配置属性字符编码-encoding
- org.springframework.core.io.support.PropertySourceFactory

适配对象- org.springframework.core.env.CompositePropertySource

## 源码分析

