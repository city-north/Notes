# 020-EnvironmentChangeEvent-当Environment配置属性发生变化时

[TOC]

## 一言蔽之

环境发生变化时， 接收到此事件表示应用里的配置数据已经发生改变， EnvironmentChangeEvent事件里维护了一个配置项Key的集合， 当配置动态刷新后， 配置值发生变化后， key会设置到事件的key集合中

