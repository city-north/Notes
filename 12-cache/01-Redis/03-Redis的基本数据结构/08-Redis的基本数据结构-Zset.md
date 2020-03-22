# Redis 有序集合(sorted set)

Redis 有序集合和集合一样也是string类型元素的集合,且不允许重复的成员。

不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

有序集合的成员是唯一的,但分数(score)却可以重复。

集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。 集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。

### 实例

```
redis 127.0.0.1:6379> ZADD runoobkey 1 redis
(integer) 1
redis 127.0.0.1:6379> ZADD runoobkey 2 mongodb
(integer) 1
redis 127.0.0.1:6379> ZADD runoobkey 3 mysql
(integer) 1
redis 127.0.0.1:6379> ZADD runoobkey 3 mysql
(integer) 0
redis 127.0.0.1:6379> ZADD runoobkey 4 mysql
(integer) 0
redis 127.0.0.1:6379> ZRANGE runoobkey 0 10 WITHSCORES

1) "redis"
2) "1"
3) "mongodb"
4) "2"
5) "mysql"
6) "4"
```

在以上实例中我们通过命令 **ZADD** 向 redis 的有序集合中添加了三个值并关联上分数。

## Redis 有序集合命令

下表列出了 redis 有序集合的基本命令:

| 序号 | 命令及描述       |                                                              |
| :--- | :--------------- | ------------------------------------------------------------ |
| 1    | zadd             | 向有序集合添加一个或多个成员，或者更新已存在成员的分数       |
| 2    | zcard            | 获取有序集合的成员数                                         |
| 3    | zcount           | 计算在有序集合中指定区间分数的成员数                         |
| 4    | zincrby          | 有序集合中对指定成员的分数加上增量 increment                 |
| 5    | zinterstore      | 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中 |
| 6    | zlexcount        | 在有序集合中计算指定字典区间内成员数量                       |
| 7    | zrange           | 通过索引区间返回有序集合指定区间内的成员                     |
| 8    | zrangebylex      | 通过字典区间返回有序集合的成员                               |
| 9    | zrangebyscore    | 通过分数返回有序集合指定区间内的成员                         |
| 10   | zrank            | 返回有序集合中指定成员的索引                                 |
| 11   | zrem             | 序集合中的一个或多个成员                                     |
| 12   | zremrangebylex   | 移除有序集合中给定的字典区间的所有成员                       |
| 13   | zremrangebyrank  | 移除有序集合中给定的排名区间的所有成员                       |
| 14   | zremrangebyscore | 移除有序集合中给定的分数区间的所有成员                       |
| 15   | zrevrange        | 返回有序集中指定区间内的成员，通过索引，分数从高到低         |
| 16   | zrevrangebyscore | 返回有序集中指定分数区间内的成员，分数从高到低排序           |
| 17   | zrevrank         | 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序 |
| 18   | zscore           | 返回有序集中，成员的分数值                                   |
| 19   | zunionstore      | 计算给定的一个或多个有序集的并集，并存储在新的 key 中        |
| 20   | zscan            | 迭代有序集合中的元素（包括元素成员和元素分值）               |