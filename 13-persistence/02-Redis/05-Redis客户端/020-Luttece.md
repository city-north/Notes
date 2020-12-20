# Luttece

[TOC]

## 简介

与 Jedis 相比，Lettuce 则完全克服了其线程不安全的缺点:

- Lettuce 是一个可伸缩 的线程安全的 Redis 客户端，支持同步、异步和响应式模式(Reactive)。

- 多个线程可 以共享一个连接实例，而不必担心多线程并发问题。

- 基于 Netty 框架构建，支持 Redis 的高级功能，如 Pipeline、发布订阅，事务、 Sentinel，集群，支持连接池。

- Lettuce 是 Spring Boot 2.x 默认的客户端，替换了 Jedis。

## 代码实例

#### 同步调用

```java
public class LettuceSyncTest {
    public static void main(String[] args) {
        // 创建客户端
        RedisClient client = RedisClient.create("redis://127.0.0.1:6379");
        // 线程安全的长连接，连接丢失时会自动重连
        StatefulRedisConnection<String, String> connection = client.connect();
        // 获取同步执行命令，默认超时时间为 60s
        RedisCommands<String, String> sync = connection.sync();
        // 发送get请求，获取值
        sync.set("gupao:sync","lettuce-sync-666" );
        String value = sync.get("gupao:sync");
        System.out.println("------"+value);
        //关闭连接
        connection.close();
        //关掉客户端
        client.shutdown();
    }
}
```

#### 异步调用

异步的结果使用 RedisFuture 包装，提供了大量回调的方法。

```java
public class LettuceASyncTest {
    public static void main(String[] args) {
        RedisClient client = RedisClient.create("redis://127.0.0.1:6379");
        // 线程安全的长连接，连接丢失时会自动重连
        StatefulRedisConnection<String, String> connection = client.connect();
        // 获取异步执行命令api
        RedisAsyncCommands<String, String> commands = connection.async();
        // 获取RedisFuture<T>
        commands.set("gupao:async","lettuce-async-666");
        RedisFuture<String> future = commands.get("gupao:async");
        try {
            String value = future.get(60, TimeUnit.SECONDS);
            System.out.println("------"+value);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
```

