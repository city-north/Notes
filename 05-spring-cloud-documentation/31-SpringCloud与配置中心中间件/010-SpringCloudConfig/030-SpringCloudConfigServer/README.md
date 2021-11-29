# 020-SpringCloudConfig配置动态刷新阶段

[TOC]

## 简介

SpringCloud 提供了配置相关的规范, 其自身也拥有一套 SpringCloud Config Server 和Client 组件, 用于配置的读取和动态刷新

Spring Cloud Config Server 相当于存储配置的一个配置中心, 对外也提供 HTTP API用于配置的获取, 若想使用 ConfigServer , 只需要启动类上使用 @EnableConfigServer即可