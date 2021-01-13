# 020-Spring-Environment使用场景

[TOC]

## Envoronment抽象UML

![image-20210113123624597](../../assets/image-20210113123624597.png)

## 简介

- 用于属性占位符处理

占位符是一种比较通用的编程手段, 防止我们在一些配置尤其是一些属性上面进行硬编码,主要有接口 PropertyResolver 和 ConfigurablePropertyResolver 提供这些行为

- 用于转换Spring配置属性类型 

配置存储形式一般是key-value , 通常是文本形式表达, 这就涉及到字符串属性->指定类型 , 主要由 `PropertyResolver.getProperty(String key, Class<T> targetType)`提供相关功能 , 

- 用于存储Spring配置属性源(PropertySource)

ConfigurableEnvironment 接口提供,主要提供的是PropertySource的获取

- 用于Profiles状态的维护

ConfigurableEnvironment 和 Envoronment 提供,主要维护相关的激活或者默认Profiles

