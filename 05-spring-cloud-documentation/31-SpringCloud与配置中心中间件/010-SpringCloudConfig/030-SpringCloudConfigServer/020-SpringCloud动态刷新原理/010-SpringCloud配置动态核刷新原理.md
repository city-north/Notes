# 010-SpringCloud配置动态核刷新原理

[TOC]

## 基础知识

 [020-SpringCloudConfig配置动态刷新阶段.md](../../020-SpringCloudConfig获取配置的原理/020-SpringCloudConfig配置动态刷新阶段.md) 

## SpringCloud动态刷新的本质

SpringCloud动态刷新的本质是通过事件触发,

- 让客户端再一次从配置文件拉取最新的配置
- 配合@RefreshScope重新刷新Bean(销毁Bean后重新创建)
- 重新刷新Bean会重新读取一遍配置, 以达到配置动态刷新的目的

## 带来的问题

- Spring Cloud提供的 SpringCloud ConfigServer /Client 组件分布式环境下需要配合SpringCloudBus(消息总线),来达到多节点配置动态刷新的目的, 因此需要额外的消息中间件组件
- @RefreshScope引起的SpringBean刷新可能会与其他组件冲突, 比如 SpringContext模块的scheduling调度功能在Spring Bean被刷新后会失去作用

## 方剑有话说

方剑认为配置管理应该专注在配置上, 而不应该有其他操作, 比如

- 需要引入消息中间件或者
- 需要重新刷新SpringBean等操作

## Nacos内部的配置动态刷新原理

通过客户端维护长轮询的任务, 定时拉取发生变更的配置, 然后将最新的数据推送到客户端的Listener的持有者, 长轮询实现源流

#### 长轮询的实现原理

 [010-长轮询实现原理.md](../../../020-SpringCloudAlibabaNacosConfig/010-010-Nacos内部的配置动态刷新原理/010-长轮询实现原理.md) 