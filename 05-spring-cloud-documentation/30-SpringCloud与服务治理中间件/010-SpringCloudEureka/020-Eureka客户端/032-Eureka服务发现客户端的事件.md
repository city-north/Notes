# 032-Eureka服务发现客户端的事件

[TOC]

## Eureka的事件

Eureka中的事件模式属于观察者模式，事件监听器将监听Client的服务实例信息变化，触发对应的处理事件，下图为Eureka事件的类图：

<img src="../../../../assets/image-20201011204221646.png" alt="image-20201011204221646" style="zoom:50%;" />

## SpringCloudCommon包中的事件

![image-20210128145416324](../../../../assets/image-20210128145416324.png)