# 010-连接RabbitMQ

---
[TOC]

## 连接RabbitMQ的典型程序

```java
ConnectionFactory factory = new ConnectionFactory();
// 连接IP
factory.setHost("127.0.0.1");
// 连接端口
factory.setPort(5673);
// 虚拟机
factory.setVirtualHost("/");
// 用户
factory.setUsername("guest");
factory.setPassword("guest");

// 建立连接
Connection conn = factory.newConnection();
// 创建消息通道
Channel channel = conn.createChannel();

// 发送消息
String msg = "Hello world, Rabbit MQ";

// String exchange, String routingKey, BasicProperties props, byte[] body
for(int i = 0; i < 5; i++) {
  channel.basicPublish(EXCHANGE_NAME, "gupao.best", null, msg.getBytes());
}

channel.close();
conn.close();
```

#### 值得注意的是

Connection可以用来创建多个Channel实例，但是Channel实例不能在线程间共享，应用程序应该为每一个线程开辟一个Channel。

某些情况下Channel的操作可以并发运行，但是在其他情况下会导致在网络上出现错误的通信帧交错，同时也会影响发送方确认（publisher confirm）机制的运行所以多线程间共享Channel实例是非线程安全的。

#### 判断是否开启

Channel或者Connection中有个isOpen方法可以用来检测其是否已处于开启状态。

但并不推荐在生产环境的代码上使用isOpen方法，这个方法的返回值依赖于shutdownCause的存在，有可能会产生竞争

```java
public boolean isOpen() {
	synchronized(this.monitor) {
		return this.shutdownCause == null;
	}
}
```