# 020-SpringCloudConfig获取配置的原理

[TOC]

## SpringCloud Bootstrap概念

在 Bootstrap 过程中, 会构建 ApplicationContext, 这个ApplicationContext加载配置的过程基于bootstrap.properties , boostrap.yml

- 在加载文件的过程中, SpringCloud有一套机制(PropertySourceLocator)来构造数据源 PropertySource. 其余过程跟Spring Boot是一致的
- Bootstrap 阶段构造的ApplicationContext会作为正常阶段的ApplicationContext的父(parent)
- 如果从子ApplicationContext

