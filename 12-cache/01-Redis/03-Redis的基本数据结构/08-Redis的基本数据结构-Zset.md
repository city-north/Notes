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

| 序号 | 命令及描述       |                                                              | 实例 |
| :--- | :--------------- | ------------------------------------------------------------ | ---- |
| 1    | zadd             | 向有序集合添加一个或多个成员，或者更新已存在成员的分数       |      |
| 2    | zcard            | 获取有序集合的成员数                                         |      |
| 3    | zcount           | 计算在有序集合中指定区间分数的成员数                         |      |
| 4    | zincrby          | 有序集合中对指定成员的分数加上增量 increment                 |      |
| 5    | zinterstore      | 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中 |      |
| 6    | zlexcount        | 在有序集合中计算指定字典区间内成员数量                       |      |
| 7    | zrange           | 通过索引区间返回有序集合指定区间内的成员                     |      |
| 8    | zrangebylex      | 通过字典区间返回有序集合的成员                               |      |
| 9    | zrangebyscore    | 通过分数返回有序集合指定区间内的成员                         |      |
| 10   | zrank            | 返回有序集合中指定成员的索引                                 |      |
| 11   | zrem             | 序集合中的一个或多个成员                                     |      |
| 12   | zremrangebylex   | 移除有序集合中给定的字典区间的所有成员                       |      |
| 13   | zremrangebyrank  | 移除有序集合中给定的排名区间的所有成员                       |      |
| 14   | zremrangebyscore | 移除有序集合中给定的分数区间的所有成员                       |      |
| 15   | zrevrange        | 返回有序集中指定区间内的成员，通过索引，分数从高到低         |      |
| 16   | zrevrangebyscore | 返回有序集中指定分数区间内的成员，分数从高到低排序           |      |
| 17   | zrevrank         | 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序 |      |
| 18   | zscore           | 返回有序集中，成员的分数值                                   |      |
| 19   | zunionstore      | 计算给定的一个或多个有序集的并集，并存储在新的 key 中        |      |
| 20   | zscan            | 迭代有序集合中的元素（包括元素成员和元素分值）               |      |

# ZRANGEBYSCORE

zrangebyscore

语法:

返回有序集 `key` 中，所有 `score` 值介于 `min` 和 `max` 之间(包括等于 `min` 或 `max` )的成员。有序集成员按 `score` 值递增(从小到大)次序排列。

具有相同 `score` 值的成员按字典序([lexicographical order](http://en.wikipedia.org/wiki/Lexicographical_order))来排列(该属性是有序集提供的，不需要额外的计算)。

可选的 `LIMIT` 参数指定返回结果的数量及区间(就像SQL中的 `SELECT LIMIT offset, count` )，注意当 `offset` 很大时，定位 `offset` 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。

可选的 `WITHSCORES` 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 `score` 值一起返回。 该选项自 Redis 2.0 版本起可用。

##### 区间及无限

`min` 和 `max` 可以是 `-inf` 和 `+inf` ，这样一来，你就可以在不知道有序集的最低和最高 `score` 值的情况下，使用 [ZRANGEBYSCORE](http://redisdoc.com/sorted_set/zrangebyscore.html#zrangebyscore) 这类命令。

默认情况下，区间的取值使用[闭区间](http://zh.wikipedia.org/wiki/區間) (小于等于或大于等于)，你也可以通过给参数前增加 `(` 符号来使用可选的[开区间](http://zh.wikipedia.org/wiki/區間) (小于或大于)。

举个例子：

```
ZRANGEBYSCORE zset (1 5
```

返回所有符合条件 `1 < score <= 5` 的成员，而

```
ZRANGEBYSCORE zset (5 (10
```

则返回所有符合条件 `5 < score < 10` 的成员。

##### 返回值

指定区间内，带有 `score` 值(可选)的有序集成员的列表。

##### 代码实例

```
redis> ZADD salary 2500 jack                        # 测试数据
(integer) 0
redis> ZADD salary 5000 tom
(integer) 0
redis> ZADD salary 12000 peter
(integer) 0

redis> ZRANGEBYSCORE salary -inf +inf               # 显示整个有序集
1) "jack"
2) "tom"
3) "peter"

redis> ZRANGEBYSCORE salary -inf +inf WITHSCORES    # 显示整个有序集及成员的 score 值
1) "jack"
2) "2500"
3) "tom"
4) "5000"
5) "peter"
6) "12000"

redis> ZRANGEBYSCORE salary -inf 5000 WITHSCORES    # 显示工资 <=5000 的所有成员
1) "jack"
2) "2500"
3) "tom"
4) "5000"

redis> ZRANGEBYSCORE salary (5000 400000            # 显示工资大于 5000 小于等于 400000 的成员
1) "peter"
```

## ZREVRANGEBYSCORE

zrevangebyscore

> 可用版本： >= 2.2.0
>
> 时间复杂度: O(log(N)+M)， `N` 为有序集的基数， `M` 为结果集的基数。

返回有序集 `key` 中， `score` 值介于 `max` 和 `min` 之间(默认包括等于 `max` 或 `min` )的所有的成员。有序集成员按 `score` 值递减(从大到小)的次序排列。

具有相同 `score` 值的成员按字典序的逆序([reverse lexicographical order](http://en.wikipedia.org/wiki/Lexicographical_order) )排列。

除了成员按 `score` 值递减的次序排列这一点外， [ZREVRANGEBYSCORE](http://redisdoc.com/sorted_set/zrevrangebyscore.html#zrevrangebyscore) 命令的其他方面和 [ZRANGEBYSCORE key min max [WITHSCORES\] [LIMIT offset count]](http://redisdoc.com/sorted_set/zrangebyscore.html#zrangebyscore) 命令一样。

## 返回值

指定区间内，带有 `score` 值(可选)的有序集成员的列表。

## 代码示例

```
redis > ZADD salary 10086 jack
(integer) 1
redis > ZADD salary 5000 tom
(integer) 1
redis > ZADD salary 7500 peter
(integer) 1
redis > ZADD salary 3500 joe
(integer) 1

redis > ZREVRANGEBYSCORE salary +inf -inf   # 逆序排列所有成员
1) "jack"
2) "peter"
3) "tom"
4) "joe"

redis > ZREVRANGEBYSCORE salary 10000 2000  # 逆序排列薪水介于 10000 和 2000 之间的成员
1) "peter"
2) "tom"
3) "joe"
```

## ZRANK key member

> 可用版本： >= 2.0.0
>
> 时间复杂度: O(log(N))

返回有序集 `key` 中成员 `member` 的排名。其中有序集成员按 `score` 值递增(从小到大)顺序排列。

排名以 `0` 为底，也就是说， `score` 值最小的成员排名为 `0` 。

使用 [ZREVRANK key member](http://redisdoc.com/sorted_set/zrevrank.html#zrevrank) 命令可以获得成员按 `score` 值递减(从大到小)排列的排名。

#### 返回值

如果 `member` 是有序集 `key` 的成员，返回 `member` 的排名。 如果 `member` 不是有序集 `key` 的成员，返回 `nil` 。

#### 代码示例

```
redis> ZRANGE salary 0 -1 WITHSCORES        # 显示所有成员及其 score 值
1) "peter"
2) "3500"
3) "tom"
4) "4000"
5) "jack"
6) "5000"

redis> ZRANK salary tom                     # 显示 tom 的薪水排名，第二
(integer) 1
```

## ZREVRANK key member

> 可用版本： >= 2.0.0
>
> 时间复杂度: O(log(N))

返回有序集 `key` 中成员 `member` 的排名。其中有序集成员按 `score` 值递减(从大到小)排序。

排名以 `0` 为底，也就是说， `score` 值最大的成员排名为 `0` 。

使用 [ZRANK key member](http://redisdoc.com/sorted_set/zrank.html#zrank) 命令可以获得成员按 `score` 值递增(从小到大)排列的排名。

#### 返回值

如果 `member` 是有序集 `key` 的成员，返回 `member` 的排名。 如果 `member` 不是有序集 `key` 的成员，返回 `nil` 。

#### 代码示例

```
redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES     # 测试数据
1) "jack"
2) "2000"
3) "peter"
4) "3500"
5) "tom"
6) "5000"

redis> ZREVRANK salary peter     # peter 的工资排第二
(integer) 1

redis> ZREVRANK salary tom       # tom 的工资最高
(integer) 0
```

## ZREM key member [member …]

> 可用版本： >= 1.2.0
>
> 时间复杂度: O(M*log(N))， `N` 为有序集的基数， `M` 为被成功移除的成员的数量。

移除有序集 `key` 中的一个或多个成员，不存在的成员将被忽略。

当 `key` 存在但不是有序集类型时，返回一个错误。

Note

在 Redis 2.4 版本以前， [ZREM](http://redisdoc.com/sorted_set/zrem.html#zrem) 每次只能删除一个元素。

#### 返回值

被成功移除的成员的数量，不包括被忽略的成员。

#### 代码示例

```
# 测试数据

redis> ZRANGE page_rank 0 -1 WITHSCORES
1) "bing.com"
2) "8"
3) "baidu.com"
4) "9"
5) "google.com"
6) "10"


# 移除单个元素

redis> ZREM page_rank google.com
(integer) 1

redis> ZRANGE page_rank 0 -1 WITHSCORES
1) "bing.com"
2) "8"
3) "baidu.com"
4) "9"


# 移除多个元素

redis> ZREM page_rank baidu.com bing.com
(integer) 2

redis> ZRANGE page_rank 0 -1 WITHSCORES
(empty list or set)


# 移除不存在元素

redis> ZREM page_rank non-exists-element
(integer) 0
```

## ZREMRANGEBYRANK key start stop

> 可用版本： >= 2.0.0
>
> 时间复杂度: O(log(N)+M)， `N` 为有序集的基数，而 `M` 为被移除成员的数量。

移除有序集 `key` 中，指定排名(rank)区间内的所有成员。

区间分别以下标参数 `start` 和 `stop` 指出，包含 `start` 和 `stop` 在内。

下标参数 `start` 和 `stop` 都以 `0` 为底，也就是说，以 `0` 表示有序集第一个成员，以 `1` 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 `-1` 表示最后一个成员， `-2` 表示倒数第二个成员，以此类推。

#### 返回值

被移除成员的数量。

#### 代码示例

```
redis> ZADD salary 2000 jack
(integer) 1
redis> ZADD salary 5000 tom
(integer) 1
redis> ZADD salary 3500 peter
(integer) 1

redis> ZREMRANGEBYRANK salary 0 1       # 移除下标 0 至 1 区间内的成员
(integer) 2

redis> ZRANGE salary 0 -1 WITHSCORES    # 有序集只剩下一个成员
1) "tom"
2) "5000"
```

## ZREMRANGEBYSCORE key min max

> 可用版本： >= 1.2.0
>
> 时间复杂度： O(log(N)+M)， `N` 为有序集的基数，而 `M` 为被移除成员的数量。

移除有序集 `key` 中，所有 `score` 值介于 `min` 和 `max` 之间(包括等于 `min` 或 `max` )的成员。

自版本2.1.6开始， `score` 值等于 `min` 或 `max` 的成员也可以不包括在内，详情请参见 [ZRANGEBYSCORE key min max [WITHSCORES\] [LIMIT offset count]](http://redisdoc.com/sorted_set/zrangebyscore.html#zrangebyscore) 命令。

#### 返回值

被移除成员的数量。

#### 代码示例

```
redis> ZRANGE salary 0 -1 WITHSCORES          # 显示有序集内所有成员及其 score 值
1) "tom"
2) "2000"
3) "peter"
4) "3500"
5) "jack"
6) "5000"

redis> ZREMRANGEBYSCORE salary 1500 3500      # 移除所有薪水在 1500 到 3500 内的员工
(integer) 2

redis> ZRANGE salary 0 -1 WITHSCORES          # 剩下的有序集成员
1) "jack"
2) "5000"
```

## ZRANGEBYLEX key min max [LIMIT offset count]

> 可用版本： >= 2.8.9
>
> 时间复杂度：O(log(N)+M)， 其中 N 为有序集合的元素数量， 而 M 则是命令返回的元素数量。 如果 M 是一个常数（比如说，用户总是使用 `LIMIT` 参数来返回最先的 10 个元素）， 那么命令的复杂度也可以看作是 O(log(N)) 。

当有序集合的所有成员都具有相同的分值时， 有序集合的元素会根据成员的字典序（lexicographical ordering）来进行排序， 而这个命令则可以返回给定的有序集合键 `key` 中， 值介于 `min` 和 `max` 之间的成员。

如果有序集合里面的成员带有不同的分值， 那么命令返回的结果是未指定的（unspecified）。

命令会使用 C 语言的 `memcmp()` 函数， 对集合中的每个成员进行逐个字节的对比（byte-by-byte compare）， 并按照从低到高的顺序， 返回排序后的集合成员。 如果两个字符串有一部分内容是相同的话， 那么命令会认为较长的字符串比较短的字符串要大。

可选的 `LIMIT offset count` 参数用于获取指定范围内的匹配元素 （就像 SQL 中的 `SELECT LIMIT offset count` 语句）。 需要注意的一点是， 如果 `offset` 参数的值非常大的话， 那么命令在返回结果之前， 需要先遍历至 `offset` 所指定的位置， 这个操作会为命令加上最多 O(N) 复杂度。

#### 如何指定范围区间

合法的 `min` 和 `max` 参数必须包含 `(` 或者 `[` ， 其中 `(` 表示开区间（指定的值不会被包含在范围之内）， 而 `[` 则表示闭区间（指定的值会被包含在范围之内）。

特殊值 `+` 和 `-` 在 `min` 参数以及 `max` 参数中具有特殊的意义， 其中 `+` 表示正无限， 而 `-` 表示负无限。 因此， 向一个所有成员的分值都相同的有序集合发送命令 `ZRANGEBYLEX - +` ， 命令将返回有序集合中的所有元素。

#### 返回值

数组回复：一个列表，列表里面包含了有序集合在指定范围内的成员。

#### 代码示例

```
redis> ZADD myzset 0 a 0 b 0 c 0 d 0 e 0 f 0 g
(integer) 7

redis> ZRANGEBYLEX myzset - [c
1) "a"
2) "b"
3) "c"

redis> ZRANGEBYLEX myzset - (c
1) "a"
2) "b"

redis> ZRANGEBYLEX myzset [aaa (g
1) "b"
2) "c"
3) "d"
4) "e"
5) "f"
```

## ZLEXCOUNT key min max

> 可用版本： >= 2.8.9
>
> 时间复杂度： O(log(N))，其中 N 为有序集合包含的元素数量。

对于一个所有成员的分值都相同的有序集合键 `key` 来说， 这个命令会返回该集合中， 成员介于 `min` 和 `max` 范围内的元素数量。

这个命令的 `min` 参数和 `max` 参数的意义和 [ZRANGEBYLEX key min max [LIMIT offset count\]](http://redisdoc.com/sorted_set/zrangebylex.html#zrangebylex) 命令的 `min` 参数和 `max` 参数的意义一样。

#### 返回值

整数回复：指定范围内的元素数量。

#### 代码示例

```
redis> ZADD myzset 0 a 0 b 0 c 0 d 0 e
(integer) 5

redis> ZADD myzset 0 f 0 g
(integer) 2

redis> ZLEXCOUNT myzset - +
(integer) 7

redis> ZLEXCOUNT myzset [b [f
(integer) 5
```

## ZREMRANGEBYLEX key min max

> 可用版本： >= 2.8.9
>
> 时间复杂度： O(log(N)+M)， 其中 N 为有序集合的元素数量， 而 M 则为被移除的元素数量。

对于一个所有成员的分值都相同的有序集合键 `key` 来说， 这个命令会移除该集合中， 成员介于 `min` 和 `max` 范围内的所有元素。

这个命令的 `min` 参数和 `max` 参数的意义和 [ZRANGEBYLEX key min max [LIMIT offset count\]](http://redisdoc.com/sorted_set/zrangebylex.html#zrangebylex) 命令的 `min` 参数和 `max` 参数的意义一样。

#### 返回值

整数回复：被移除的元素数量。

#### 代码示例

```
redis> ZADD myzset 0 aaaa 0 b 0 c 0 d 0 e
(integer) 5

redis> ZADD myzset 0 foo 0 zap 0 zip 0 ALPHA 0 alpha
(integer) 5

redis> ZRANGE myzset 0 -1
1) "ALPHA"
2) "aaaa"
3) "alpha"
4) "b"
5) "c"
6) "d"
7) "e"
8) "foo"
9) "zap"
10) "zip"

redis> ZREMRANGEBYLEX myzset [alpha [omega
(integer) 6

redis> ZRANGE myzset 0 -1
1) "ALPHA"
2) "aaaa"
3) "zap"
4) "zip"
```

## ZUNIONSTORE destination numkeys key [key …] [WEIGHTS weight [weight …]] [AGGREGATE SUM|MIN|MAX]

> 可用版本：>= 2.0.0
>
> 时间复杂度: O(N)+O(M log(M))， `N` 为给定有序集基数的总和， `M` 为结果集的基数。

计算给定的一个或多个有序集的并集，其中给定 `key` 的数量必须以 `numkeys` 参数指定，并将该并集(结果集)储存到 `destination` 。

默认情况下，结果集中某个成员的 `score` 值是所有给定集下该成员 `score` 值之 *和* 。

#### WEIGHTS

使用 `WEIGHTS` 选项，你可以为 *每个* 给定有序集 *分别* 指定一个乘法因子(multiplication factor)，每个给定有序集的所有成员的 `score` 值在传递给聚合函数(aggregation function)之前都要先乘以该有序集的因子。

如果没有指定 `WEIGHTS` 选项，乘法因子默认设置为 `1` 。

#### AGGREGATE

使用 `AGGREGATE` 选项，你可以指定并集的结果集的聚合方式。

默认使用的参数 `SUM` ，可以将所有集合中某个成员的 `score` 值之 *和* 作为结果集中该成员的 `score` 值；使用参数 `MIN` ，可以将所有集合中某个成员的 *最小* `score` 值作为结果集中该成员的 `score` 值；而参数 `MAX` 则是将所有集合中某个成员的 *最大* `score` 值作为结果集中该成员的 `score` 值。

#### 返回值

保存到 `destination` 的结果集的基数。

#### 代码示例

```
redis> ZRANGE programmer 0 -1 WITHSCORES
1) "peter"
2) "2000"
3) "jack"
4) "3500"
5) "tom"
6) "5000"

redis> ZRANGE manager 0 -1 WITHSCORES
1) "herry"
2) "2000"
3) "mary"
4) "3500"
5) "bob"
6) "4000"

redis> ZUNIONSTORE salary 2 programmer manager WEIGHTS 1 3   # 公司决定加薪。。。除了程序员。。。
(integer) 6

redis> ZRANGE salary 0 -1 WITHSCORES
1) "peter"
2) "2000"
3) "jack"
4) "3500"
5) "tom"
6) "5000"
7) "herry"
8) "6000"
9) "mary"
10) "10500"
11) "bob"
12) "12000"
```

## ZINTERSTORE destination numkeys key [key …] [WEIGHTS weight [weight …]] [AGGREGATE SUM|MIN|MAX]

> 可用版本： >= 2.0.0
>
> 时间复杂度: O(N*K)+O(M*log(M))， `N` 为给定 `key` 中基数最小的有序集， `K` 为给定有序集的数量， `M` 为结果集的基数。

计算给定的一个或多个有序集的交集，其中给定 `key` 的数量必须以 `numkeys` 参数指定，并将该交集(结果集)储存到 `destination` 。

默认情况下，结果集中某个成员的 `score` 值是所有给定集下该成员 `score` 值之和.

关于 `WEIGHTS` 和 `AGGREGATE` 选项的描述，参见 [ZUNIONSTORE destination numkeys key [key …\] [WEIGHTS weight [weight …]] [AGGREGATE SUM|MIN|MAX](http://redisdoc.com/sorted_set/zunionstore.html#zunionstore) 命令。

#### 返回值

保存到 `destination` 的结果集的基数。

#### 代码示例

```
redis > ZADD mid_test 70 "Li Lei"
(integer) 1
redis > ZADD mid_test 70 "Han Meimei"
(integer) 1
redis > ZADD mid_test 99.5 "Tom"
(integer) 1

redis > ZADD fin_test 88 "Li Lei"
(integer) 1
redis > ZADD fin_test 75 "Han Meimei"
(integer) 1
redis > ZADD fin_test 99.5 "Tom"
(integer) 1

redis > ZINTERSTORE sum_point 2 mid_test fin_test
(integer) 3

redis > ZRANGE sum_point 0 -1 WITHSCORES     # 显示有序集内所有成员及其 score 值
1) "Han Meimei"
2) "145"
3) "Li Lei"
4) "158"
5) "Tom"
6) "199"
```