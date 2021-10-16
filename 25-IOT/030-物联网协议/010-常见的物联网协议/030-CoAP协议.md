# 030-CoAP协议

[TOC]

## 什么是CoAP协议

CoAP（Constrained Application Protocol）协议是一种运行在资源比较紧张的设备上的协议。

**和MQTT-SN协议一样，CoAP协议通常也是运行在UDP协议上的。**

- CoAP协议设计得非常小巧，最小的数据包只有4个字节。
- CoAP协议采用C/S架构，使用类似于HTTP协议的请求-响应的交互模式。

设备可以通过类似于coap://192.168.1.150:5683/2ndfloor/temperature的URL来标识一个实体，并使用类似于HTTP的PUT、GET、POST、DELET请求指令来获取或者修改这个实体的状态。

- 同时，CoAP提供一种观察模式，观察者可以通过OBSERVE指令向CoAP服务器指明观察的实体对象。当实体对象的状态发生变化时，观察者就可以收到实体对象的最新状态，类似于MQTT协议中的订阅功能。CoAP协议的通信模型如图所示。
  ￼

![](http://assets.processon.com/chart_image/616ac80bf346fb06a9f22cfc.png)



