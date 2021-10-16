# 020-MQTT-SN协议

[TOC]

## 什么是MQTT-SN

MQTT-SN（MQTT for Sensor Network）协议是MQTT协议的传感器版本。

MQTT协议虽然是轻量的应用层协议，但是MQTT协议是运行于TCP协议栈之上的，TCP协议对于某些计算能力和电量非常有限的设备来说，比如传感器，就不太适用了。

## MQTT-SN特点

- MQTT-SN运行在UDP协议上，同时保留了MQTT协议的大部分信令和特性，如订阅和发布等。

- MQTT-SN协议引入了MQTT-SN网关这一角色，网关负责把MQTT-SN协议转换为MQTT协议，并和远端的MQTT Broker进行通信。

- MQTT-SN协议支持网关的自动发现。MQTT-SN协议的通信模型如图所示。

![](http://assets.processon.com/chart_image/616ac5ecf346fb06a9f22a67.png)

