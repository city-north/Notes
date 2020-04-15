# BGSAVE

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(N)， `N` 为要保存到数据库中的 key 的数量。

在后台异步(Asynchronously)保存当前数据库的数据到磁盘。

[BGSAVE](http://redisdoc.com/persistence/bgsave.html#bgsave) 命令执行之后立即返回 `OK` ，然后 Redis fork 出一个新子进程，原来的 Redis 进程(父进程)继续处理客户端请求，而子进程则负责将数据保存到磁盘，然后退出。

客户端可以通过 [LASTSAVE](http://redisdoc.com/persistence/lastsave.html#lastsave) 命令查看相关信息，判断 [BGSAVE](http://redisdoc.com/persistence/bgsave.html#bgsave) 命令是否执行成功。

请移步 [持久化文档](http://redis.io/topics/persistence) 查看更多相关细节。

## 返回值

反馈信息。

## 代码示例

```
redis> BGSAVE
Background saving started
```