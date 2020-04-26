# Hyperloglog

**Hyperloglog 提供存在误差的去重计数方案**

虽然不精确,但是也不是非常离谱,标准误差是 0.81%

## 典型的使用场景

如果是同级 PV 访客, 给每个 网页配一个独立 Redis 计数器就可以了, 把这个计数器的 key 后缀加上当天的日期,这样来一个请求,执行 incrby 指令一次,最终就可以统计所有的 PV 数据

**但是如果要去重怎么办?**

同级网页的 UV (Unique Visit )独立访客信息 , 用户一天之内多次访问请求,智能技术一次,这就要求每个网页请求都需要带上用户 ID,无论是登录用户还是非登录用户都需要唯一 ID 来标识

如果访问量很大,set 就 hi 耗费非常多的空间, 所以使用 HyperLogLog 来提供不精确的去重技术方案

## PFADD key element [element …]

> 可用版本： >= 2.8.9
>
> 时间复杂度： 每添加一个元素的复杂度为 O(1) 。

将任意数量的元素添加到指定的 HyperLogLog 里面。

作为这个命令的副作用， HyperLogLog 内部可能会被更新， 以便反映一个不同的唯一元素估计数量（也即是集合的基数）。

如果 HyperLogLog 估计的近似基数（approximated cardinality）在命令执行之后出现了变化， 那么命令返回 `1` ， 否则返回 `0` 。 如果命令执行时给定的键不存在， 那么程序将先创建一个空的 HyperLogLog 结构， 然后再执行命令。

调用 [PFADD key element [element …\]](http://redisdoc.com/hyperloglog/pfadd.html#pfadd) 命令时可以只给定键名而不给定元素：

- 如果给定键已经是一个 HyperLogLog ， 那么这种调用不会产生任何效果；
- 但如果给定的键不存在， 那么命令会创建一个空的 HyperLogLog ， 并向客户端返回 `1` 。

要了解更多关于 HyperLogLog 数据结构的介绍知识， 请查阅 [PFCOUNT key [key …\]](http://redisdoc.com/hyperloglog/pfcount.html#pfcount) 命令的文档。

#### 返回值

整数回复： 如果 HyperLogLog 的内部储存被修改了， 那么返回 1 ， 否则返回 0 。

#### 代码示例

```
redis> PFADD  databases  "Redis"  "MongoDB"  "MySQL"
(integer) 1

redis> PFCOUNT  databases
(integer) 3

redis> PFADD  databases  "Redis"    # Redis 已经存在，不必对估计数量进行更新
(integer) 0

redis> PFCOUNT  databases    # 元素估计数量没有变化
(integer) 3

redis> PFADD  databases  "PostgreSQL"    # 添加一个不存在的元素
(integer) 1

redis> PFCOUNT  databases    # 估计数量增一
4
```

## PFCOUNT key [key …]

> 可用版本： >= 2.8.9
>
> 时间复杂度： 当命令作用于单个 HyperLogLog 时， 复杂度为 O(1) ， 并且具有非常低的平均常数时间。 当命令作用于 N 个 HyperLogLog 时， 复杂度为 O(N) ， 常数时间也比处理单个 HyperLogLog 时要大得多。

当 [PFCOUNT key [key …\]](http://redisdoc.com/hyperloglog/pfcount.html#pfcount) 命令作用于单个键时， 返回储存在给定键的 HyperLogLog 的近似基数， 如果键不存在， 那么返回 `0` 。

当 [PFCOUNT key [key …\]](http://redisdoc.com/hyperloglog/pfcount.html#pfcount) 命令作用于多个键时， 返回所有给定 HyperLogLog 的并集的近似基数， 这个近似基数是通过将所有给定 HyperLogLog 合并至一个临时 HyperLogLog 来计算得出的。

通过 HyperLogLog 数据结构， 用户可以使用少量固定大小的内存， 来储存集合中的唯一元素 （每个 HyperLogLog 只需使用 12k 字节内存，以及几个字节的内存来储存键本身）。

命令返回的可见集合（observed set）基数并不是精确值， 而是一个带有 0.81% 标准错误（standard error）的近似值。

举个例子， 为了记录一天会执行多少次各不相同的搜索查询， 一个程序可以在每次执行搜索查询时调用一次 [PFADD key element [element …\]](http://redisdoc.com/hyperloglog/pfadd.html#pfadd) ， 并通过调用 [PFCOUNT key [key …\]](http://redisdoc.com/hyperloglog/pfcount.html#pfcount) 命令来获取这个记录的近似结果。

#### 返回值

整数回复： 给定 HyperLogLog 包含的唯一元素的近似数量。

#### 代码示例

```
redis> PFADD  databases  "Redis"  "MongoDB"  "MySQL"
(integer) 1

redis> PFCOUNT  databases
(integer) 3

redis> PFADD  databases  "Redis"    # Redis 已经存在，不必对估计数量进行更新
(integer) 0

redis> PFCOUNT  databases    # 元素估计数量没有变化
(integer) 3

redis> PFADD  databases  "PostgreSQL"    # 添加一个不存在的元素
(integer) 1

redis> PFCOUNT  databases    # 估计数量增一
4
```

## PFMERGE destkey sourcekey [sourcekey …]

> 可用版本： >= 2.8.9
>
> 时间复杂度： O(N) ， 其中 N 为被合并的 HyperLogLog 数量， 不过这个命令的常数复杂度比较高。

将多个 HyperLogLog 合并（merge）为一个 HyperLogLog ， 合并后的 HyperLogLog 的基数接近于所有输入 HyperLogLog 的可见集合（observed set）的并集。

合并得出的 HyperLogLog 会被储存在 `destkey` 键里面， 如果该键并不存在， 那么命令在执行之前， 会先为该键创建一个空的 HyperLogLog 。

#### 返回值

字符串回复：返回 `OK` 。

#### 代码示例

```
redis> PFADD  nosql  "Redis"  "MongoDB"  "Memcached"
(integer) 1

redis> PFADD  RDBMS  "MySQL" "MSSQL" "PostgreSQL"
(integer) 1

redis> PFMERGE  databases  nosql  RDBMS
OK

redis> PFCOUNT  databases
(integer) 6
```



