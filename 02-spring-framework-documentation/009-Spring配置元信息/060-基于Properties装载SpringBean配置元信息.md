# 060-基于Properties装载SpringBean配置元信息

[TOC]

## SpringProperties资源BeanDefinition解析与注册

### 核心API

- 资源
  - 字节流-Resource
  - 字符流-EncodedResource(有编码,更容易给人阅读)
- 底层
  - 存储-java.util.Properties
  - BeanDefinition解析-API内部实现
  - BeanDefinition注册-BeanDefinitionRegistry

### 核心入口

org.springframework.beans.factory.support.PropertiesBeanDefinitionReader

