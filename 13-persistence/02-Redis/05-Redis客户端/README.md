# 客户端通讯原理

客户端跟 Redis 之间 使用一种特殊的编码格式(在 AOF 文件里面我们看到了)，叫 做 Redis Serialization Protocol  (Redis 序列化协议)

特点:容易实现、解析快、可读性强。

客户端发给服务端的消息需要经过编码，服务端收到之后会按约定进行解码，反之亦然。

## 官网推荐的 Java 客户端

- Jedis
- Redission
- Luttue

