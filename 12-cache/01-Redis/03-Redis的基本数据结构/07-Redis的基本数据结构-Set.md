# Redis 集合(Set)

Redis 的 Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。

Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。

集合中最大的成员数为 2^32 - 1 (4294967295, 每个集合可存储40多亿个成员)。

### 实例

```
redis 127.0.0.1:6379> SADD runoobkey redis
(integer) 1
redis 127.0.0.1:6379> SADD runoobkey mongodb
(integer) 1
redis 127.0.0.1:6379> SADD runoobkey mysql
(integer) 1
redis 127.0.0.1:6379> SADD runoobkey mysql
(integer) 0
redis 127.0.0.1:6379> SMEMBERS runoobkey

1) "mysql"
2) "mongodb"
3) "redis"
```

在以上实例中我们通过 **SADD** 命令向名为 **runoobkey** 的集合插入的三个元素。

| 序号 | 命令及描述  |                                                     |
| :--- | :---------- | --------------------------------------------------- |
| 1    | SADD        | 向集合添加一个或多个成员                            |
| 2    | SCARD       | 获取集合的成员数                                    |
| 3    | SDIFF       | 返回给定所有集合的差集                              |
| 4    | SDIFFSTORE  | 返回给定所有集合的差集并存储在 destination 中       |
| 5    | SINTER      | 返回给定所有集合的交集                              |
| 6    | SINTERSTORE | 返回给定所有集合的交集并存储在 destination 中       |
| 7    | SISMEMBER   | 判断 member 元素是否是集合 key 的成员               |
| 8    | SMEMBERS    | 返回集合中的所有成员                                |
| 9    | SMOVE       | 将 member 元素从 source 集合移动到 destination 集合 |
| 10   | SPOP        | 移除并返回集合中的一个随机元素                      |
| 11   | SRANDMEMBER | 返回集合中一个或多个随机数                          |
| 12   | SREM        | 移除集合中一个或多个成员                            |
| 13   | SUNION      | 返回所有给定集合的并集                              |
| 14   | SUNIONSTORE | 所有给定集合的并集存储在 destination 集合中         |
| 15   | SSCAN       | 迭代集合中的元素                                    |