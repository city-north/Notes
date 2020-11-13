# Connector组件

Connector主要的职责就是接收客户端连接并接收消息报文，消息报文经由它解析后送往容器中处理。如图所示，因为存在不同的通信协议，例如HTTP协议、AJP协议等，所以我们需要不同的Connector组件，每种协议对应一个Connector组件，目前Tomcat包含HTTP和AJP两种协议的Connector。
￼￼￼![image-20201026124617278](../../../assets/image-20201026124617278.png)

上面从协议角度介绍了不同的Connector组件，而Connector组件的内部实现也会根据网络I/O的不同方式而不同分为阻塞I/O和非阻塞I/O。

下面以HTTP协议为例，看看阻塞I/O和非阻塞I/O的Connector内部实现模块有什么不同。

在阻塞I/O方式下，Connector的结构如图4.4所示。