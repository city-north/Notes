# Luttece

与 Jedis 相比，Lettuce 则完全克服了其线程不安全的缺点:Lettuce 是一个可伸缩 的线程安全的 Redis 客户端，支持同步、异步和响应式模式(Reactive)。多个线程可 以共享一个连接实例，而不必担心多线程并发问题。

基于 Netty 框架构建，支持 Redis 的高级功能，如 Pipeline、发布订阅，事务、 Sentinel，集群，支持连接池。

Lettuce 是 Spring Boot 2.x 默认的客户端，替换了 Jedis。