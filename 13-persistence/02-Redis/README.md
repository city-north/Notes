# Redis

![img](assets/640.jpeg)

## **RE**mote **DI**ctionary **S**ervice

官网介绍:https://redis.io/topics/introduction
中文网站:http://www.redis.cn

硬件层面有 CPU 的缓存;浏览器也有缓存;手机的应用也有缓存。我们把数据缓存
起来的原因就是从原始位置取数据的代价太大了，放在一个临时位置存储起来，取回就 可以快一些。

## Redis 的特性:

- 更丰富的数据类型 
- 进程内与跨进程;单机与分布式 
- 功能丰富:持久化机制、过期策略 
- 支持多种编程语言 
- 高可用，集群

## Redis的数据类型

- String
- Hash
- Set
- List
- Zset
- Hyperloglog
- Geo
- Streams

## Redis 的典型使用场景

1. 记录点赞数,评论数和点击数--hash
2. 记录用户的帖子 ID 列表(排序),便于快速显示用户的帖子列表--zset
3. 记录帖子的标题,摘要,作者和封面信息,用于列表页展示--hash
4. 记录帖子的点赞用户 ID 列表,评论 ID 列表,用于显示和去重计数---zset
5. 缓存近期热帖内容,帖子内容的空间占用比较大,减少数据库压力--hash
6. 记录帖子的相关文章 ID, 格局内容推荐相关帖子---list
7. 如果帖子 ID 是整数自增的,可以使用 Redis 来分配帖子 ID---计数器
8. 收藏集和帖子之间的关系--zset
9. 记录热榜帖子 ID 列表,总热榜和分类热榜
10. 缓存用户行为历史,过滤恶意行为-zet-hash

## 数据一致性

 [02-缓存一致性.md](06-模式以及常见问题/02-缓存一致性.md) 