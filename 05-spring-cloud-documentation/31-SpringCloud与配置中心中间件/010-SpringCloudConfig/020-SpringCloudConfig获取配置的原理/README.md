# 010-从SpringCloud配置中心获取配置的原理

[TOC]

## 简介

SpringCloud Bootstrap 阶段的概念: 

- 在Bootstrap阶段会构造一个 Bootstrap ApplicationContext , 这个 ApplicatoonContext 加载配置的过程会基于bootstrap.properties  或者 bootstrap.yml 文件 去加载文件
- SpringCloud 在加载文件时, 会有一套机制 (PropertySourceLocator接口的定义)来构建数据源 PropertySources
- Bootstrap 阶段构造的ApplicationContext会作为正常阶段的 ApplicationContext 的父(parent), 读取顺序是先读取子, 再读取父

## Bootstrap配置的优先级

我们使用配置中心前必须先对配置中心的地址进行配置, 这个地址需要在配置文件内定义, 

Spring Cloud Bootstrap优先级别高, 会先读取配置中心的配置, 这些配置, 用于加载下一个 ApplicationContext 时使用

## Bootstrap初始化阶段

#### 一言蔽之

1. spring-cloud-context 模块内部的 META-INF/spring.factory 添加一个 BootstrapApplicationListener (实现了ApplicationListener接口), 
2. 用于监听 ApplicationEnvironmentPreparedEvent事件(Environment 刚创建, 但是ApplicationContext还未创建时触发的事件)
3. 收到该事件后进入Bootstrap阶段, 从配置中心加载配置

