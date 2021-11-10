# 010-从SpringCloud配置中心获取配置的原理

[TOC]

## 简介

SpringCloud Bootstrap 阶段的概念: 

- 在Bootstrap阶段会构造一个 Bootstrap ApplicationContext , 这个 ApplicatoonContext 加载配置的过程会基于bootstrap.properties  或者 bootstrap.yml 文件 去加载文件
- SpringCloud 在加载文件时, 会有一套机制 (PropertySourceLocator接口的定义)来狗仔数据源 PropertySources
- Bootstrap 阶段构造的ApplicationContext会作为正常阶段的 ApplicationContext 的父(parent), 读取顺序是先读取子, 再读取父

