# Jedis

## 特点

Jedis 是我们最熟悉和最常用的客户端。轻量，简洁，便于集成和改造。

```
public static void main(String[] args) {
	Jedis jedis = new Jedis("127.0.0.1", 6379); 
	jedis.set("qingshan", "2673"); 
	System.out.println(jedis.get("qingshan")); 
	jedis.close();
}
```

Jedis 多个线程使用一个连接的时候线程不安全,可以使用连接池,为每个请求创建不同的连接

Jedis 有

- 4 种工作模式:单节点、分片、哨兵、集群。

- 3 种请求模式:Client、Pipeline、事务。

#### Client 模式

Client 模式就是客户端发送一个命令，阻 塞等待服务端执行，然后读取 返回结果。

#### Pipeline 模式

Pipeline 模式是一次性发送多个命令，最后一 次取回所有的返回结果，这种模式通过减少网络的往返时间和 io 读写次数，大幅度提高 通信性能。第三种是事务模式。

#### 事务模式

Transaction 模式即开启 Redis 的事务管理，事务模式开 启后，所有的命令(除了 exec，discard，multi 和 watch)到达服务端以后不会立即执 行，会进入一个等待队列。