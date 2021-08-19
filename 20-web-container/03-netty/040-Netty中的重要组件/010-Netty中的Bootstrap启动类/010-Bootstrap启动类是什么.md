# 010-Bootstrap启动类是什么

[TOC]

## 什么是Bootstrap

Bootstrap是Netty提供的一个便利的工具类， 可以通过它来完成Netty客户端或者服务器端的Netty组件的组装，以及netty程序的初始化

我们完全可以不用这个Bootstrap启动器， 手动创建通道，完成各种设置和启动， 并且注册到EventLoop

## Bootstrap两个启动类

- AbstractBootstrap
  - Bootstrap : 针对客户端
  - ServerBootstrap : 针对服务端