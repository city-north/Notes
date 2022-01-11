# 010-Redis-hash-基础命令

[TOC]

| 序号  | 命令                              | 描述                                                         |
| ----- | --------------------------------- | ------------------------------------------------------------ |
| 1     | [hdel](#hdel)                     | 删除一个或多个哈希表字段                                     |
| 2     | [hexists](#hexists)               | 查看哈希表 key 中，指定的字段是否存在。                      |
| 3     | [hget](#hget)                     | 获取存储在哈希表中指定字段的值。                             |
| 4     | [hgetall](#hgetall)               | 获取在哈希表中指定 key 的所有字段和值                        |
| 5     | **[hincrby](#hincrby)**           | **为哈希表 key 中的指定字段的整数值加上增量 increment 。**   |
| **6** | **[hincrbyfloat](#hincrbyfloat)** | **为哈希表 key 中的指定字段的浮点数值加上增量 increment** 。 |
| 7     | [hkeys](#hkeys)                   | 获取所有哈希表中的字段                                       |
| 8     | [hlen](#hlen)                     | 获取哈希表中字段的数量                                       |
| 9     | [hmget](#hmget)                   | 获取所有给定字段的值                                         |
| 10    | [hmset](#hmset)                   | 同时将多个 field-value (域-值)对设置到哈希表 key 中。        |
| 11    | [hset](#hset)                     | 将哈希表 key 中的字段 field 的值设为 value 。                |
| 12    | [hsetnx](#hsetnx)                 | 只有在字段 field 不存在时，设置哈希表字段的值。              |
| 13    | [hvals](#hvals)                   | 获取哈希表中所有值                                           |
| 14    | hscan                             | 迭代哈希表中的键值对。与 scan 一样                           |
| 15    | [HSTRLEN](#HSTRLEN)               | 返回哈希表 `key` 中， 与给定域 `field` 相关联的值的字符串长度（string length）。 |

## hset

> 可用版本： >= 2.0.0
>
> 时间复杂度： O(1)

将哈希表 `hash` 中域 `field` 的值设置为 `value` 。

如果给定的哈希表并不存在， 那么一个新的哈希表将被创建并执行 `HSET` 操作。

如果域 `field` 已经存在于哈希表中， 那么它的旧值将被新值 `value` 覆盖。

### 返回值

当 `HSET` 命令在哈希表中新创建 `field` 域并成功为它设置值时， 命令返回 `1` ； 如果域 `field` 已经存在于哈希表， 并且 `HSET` 命令成功使用新值覆盖了它的旧值， 那么命令返回 `0` 。

### 代码示例

设置一个新域：

```
redis> HSET website google "www.g.cn"
(integer) 1

redis> HGET website google
"www.g.cn"
```

对一个已存在的域进行更新：

```
redis> HSET website google "www.google.com"
(integer) 0

redis> HGET website google
"www.google.com"
```

## hsetnx

> 可用版本： >= 2.0.0
>
> 时间复杂度： O(1)

当且仅当域 `field` 尚未存在于哈希表的情况下， 将它的值设置为 `value` 。

如果给定域已经存在于哈希表当中， 那么命令将放弃执行设置操作。

如果哈希表 `hash` 不存在， 那么一个新的哈希表将被创建并执行 `HSETNX` 命令。

### 返回值

`HSETNX` 命令在设置成功时返回 `1` ， 在给定域已经存在而放弃执行设置操作时返回 `0` 。

### 代码示例

域尚未存在， 设置成功：

```
redis> HSETNX database key-value-store Redis
(integer) 1

redis> HGET database key-value-store
"Redis"
```

域已经存在， 设置未成功， 域原有的值未被改变：

```
redis> HSETNX database key-value-store Riak
(integer) 0

redis> HGET database key-value-store
"Redis"
```

## hget

> 可用版本： >= 2.0.0
>
> 时间复杂度： O(1)

返回哈希表中给定域的值。

### 返回值

`HGET` 命令在默认情况下返回给定域的值。

如果给定域不存在于哈希表中， 又或者给定的哈希表并不存在， 那么命令返回 `nil` 。

### 代码示例

域存在的情况：

```
redis> HSET homepage redis redis.com
(integer) 1

redis> HGET homepage redis
"redis.com"
```

域不存在的情况：

```
redis> HGET site mysql
(nil)
```

## hexists

> 可用版本： >= 2.0.0
>
> 时间复杂度： O(1)

检查给定域 `field` 是否存在于哈希表 `hash` 当中。

### 返回值

`HEXISTS` 命令在给定域存在时返回 `1` ， 在给定域不存在时返回 `0` 。

### 代码示例

给定域不存在：

```
redis> HEXISTS phone myphone
(integer) 0
```

给定域存在：

```
redis> HSET phone myphone nokia-1110
(integer) 1

redis> HEXISTS phone myphone
(integer) 1
```

## hdel

**HDEL key field [field …]**

删除哈希表 `key` 中的一个或多个指定域，不存在的域将被忽略。

Note

在Redis2.4以下的版本里， [HDEL](http://redisdoc.com/hash/hdel.html#hdel) 每次只能删除单个域，如果你需要在一个原子时间内删除多个域，请将命令包含在 [MULTI](http://redisdoc.com/transaction/multi.html#multi) / [EXEC](http://redisdoc.com/transaction/exec.html#exec) 块内。

- **可用版本：**

  >= 2.0.0

- **时间复杂度:**

  O(N)， `N` 为要删除的域的数量。

- **返回值:**

  被成功移除的域的数量，不包括被忽略的域。

```
# 测试数据

redis> HGETALL abbr
1) "a"
2) "apple"
3) "b"
4) "banana"
5) "c"
6) "cat"
7) "d"
8) "dog"


# 删除单个域

redis> HDEL abbr a
(integer) 1


# 删除不存在的域

redis> HDEL abbr not-exists-field
(integer) 0


# 删除多个域

redis> HDEL abbr b c
(integer) 2

redis> HGETALL abbr
1) "d"
2) "dog"
```

## hlen

**HLEN key**

返回哈希表 `key` 中域的数量。

- **时间复杂度：**

  O(1)

- **返回值：**

  哈希表中域的数量。当 `key` 不存在时，返回 `0` 。

```
redis> HSET db redis redis.com
(integer) 1

redis> HSET db mysql mysql.com
(integer) 1

redis> HLEN db
(integer) 2

redis> HSET db mongodb mongodb.org
(integer) 1

redis> HLEN db
(integer) 3
```

## HSTRLEN

**HSTRLEN key field**

返回哈希表 `key` 中， 与给定域 `field` 相关联的值的字符串长度（string length）。

如果给定的键或者域不存在， 那么命令返回 `0` 。

- **可用版本：**

  >= 3.2.0

- **时间复杂度：**

  O(1)

- **返回值：**

  一个整数。

```
redis> HMSET myhash f1 "HelloWorld" f2 "99" f3 "-256"
OK

redis> HSTRLEN myhash f1
(integer) 10

redis> HSTRLEN myhash f2
(integer) 2

redis> HSTRLEN myhash f3
(integer) 4
```

## hincrby

**HINCRBY key field increment**

为哈希表 `key` 中的域 `field` 的值加上增量 `increment` 。

增量也可以为负数，相当于对给定域进行减法操作。

如果 `key` 不存在，一个新的哈希表被创建并执行 [HINCRBY](http://redisdoc.com/hash/hincrby.html#hincrby) 命令。

如果域 `field` 不存在，那么在执行命令前，域的值被初始化为 `0` 。

对一个储存字符串值的域 `field` 执行 [HINCRBY](http://redisdoc.com/hash/hincrby.html#hincrby) 命令将造成一个错误。

本操作的值被限制在 64 位(bit)有符号数字表示之内。

- **可用版本：**

  >= 2.0.0

- **时间复杂度：**

  O(1)

- **返回值：**

  执行 [HINCRBY](http://redisdoc.com/hash/hincrby.html#hincrby) 命令之后，哈希表 `key` 中域 `field` 的值。

```
# increment 为正数

redis> HEXISTS counter page_view    # 对空域进行设置
(integer) 0

redis> HINCRBY counter page_view 200
(integer) 200

redis> HGET counter page_view
"200"


# increment 为负数

redis> HGET counter page_view
"200"

redis> HINCRBY counter page_view -50
(integer) 150

redis> HGET counter page_view
"150"


# 尝试对字符串值的域执行HINCRBY命令

redis> HSET myhash string hello,world       # 设定一个字符串值
(integer) 1

redis> HGET myhash string
"hello,world"

redis> HINCRBY myhash string 1              # 命令执行失败，错误。
(error) ERR hash value is not an integer

redis> HGET myhash string                   # 原值不变
"hello,world"
```

## hincrbyfloat

**HINCRBYFLOAT key field increment**

为哈希表 `key` 中的域 `field` 加上浮点数增量 `increment` 。

如果哈希表中没有域 `field` ，那么 [HINCRBYFLOAT](http://redisdoc.com/hash/hincrbyfloat.html#hincrbyfloat) 会先将域 `field` 的值设为 `0` ，然后再执行加法操作。

如果键 `key` 不存在，那么 [HINCRBYFLOAT](http://redisdoc.com/hash/hincrbyfloat.html#hincrbyfloat) 会先创建一个哈希表，再创建域 `field` ，最后再执行加法操作。

当以下任意一个条件发生时，返回一个错误：

- 域 `field` 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
- 域 `field` 当前的值或给定的增量 `increment` 不能解释(parse)为双精度浮点数(double precision floating point number)

[HINCRBYFLOAT](http://redisdoc.com/hash/hincrbyfloat.html#hincrbyfloat) 命令的详细功能和 [INCRBYFLOAT key increment](http://redisdoc.com/string/incrbyfloat.html#incrbyfloat) 命令类似，请查看 [INCRBYFLOAT key increment](http://redisdoc.com/string/incrbyfloat.html#incrbyfloat) 命令获取更多相关信息。

- **可用版本：**

  >= 2.6.0

- **时间复杂度：**

  O(1)

- **返回值：**

  执行加法操作之后 `field` 域的值。

```
# 值和增量都是普通小数

redis> HSET mykey field 10.50
(integer) 1
redis> HINCRBYFLOAT mykey field 0.1
"10.6"


# 值和增量都是指数符号

redis> HSET mykey field 5.0e3
(integer) 0
redis> HINCRBYFLOAT mykey field 2.0e2
"5200"


# 对不存在的键执行 HINCRBYFLOAT

redis> EXISTS price
(integer) 0
redis> HINCRBYFLOAT price milk 3.5
"3.5"
redis> HGETALL price
1) "milk"
2) "3.5"


# 对不存在的域进行 HINCRBYFLOAT

redis> HGETALL price
1) "milk"
2) "3.5"
redis> HINCRBYFLOAT price coffee 4.5   # 新增 coffee 域
"4.5"
redis> HGETALL price
1) "milk"
2) "3.5"
3) "coffee"
4) "4.5"
```

## hmset

**HMSET key field value [field value …]**

同时将多个 `field-value` (域-值)对设置到哈希表 `key` 中。

此命令会覆盖哈希表中已存在的域。

如果 `key` 不存在，一个空哈希表被创建并执行 [HMSET](http://redisdoc.com/hash/hmset.html#hmset) 操作。

- **可用版本：**

  >= 2.0.0

- **时间复杂度：**

  O(N)， `N` 为 `field-value` 对的数量。

- **返回值：**

  如果命令执行成功，返回 `OK` 。当 `key` 不是哈希表(hash)类型时，返回一个错误。

```
redis> HMSET website google www.google.com yahoo www.yahoo.com
OK

redis> HGET website google
"www.google.com"

redis> HGET website yahoo
"www.yahoo.com"
```

## hmget

**HMGET key field [field …]**

返回哈希表 `key` 中，一个或多个给定域的值。

如果给定的域不存在于哈希表，那么返回一个 `nil` 值。

因为不存在的 `key` 被当作一个空哈希表来处理，所以对一个不存在的 `key` 进行 [HMGET](http://redisdoc.com/hash/hmget.html#hmget) 操作将返回一个只带有 `nil` 值的表。

- **可用版本：**

  >= 2.0.0

- **时间复杂度：**

  O(N)， `N` 为给定域的数量。

- **返回值：**

  一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。

```
redis> HMSET pet dog "doudou" cat "nounou"    # 一次设置多个域
OK

redis> HMGET pet dog cat fake_pet             # 返回值的顺序和传入参数的顺序一样
1) "doudou"
2) "nounou"
3) (nil)                                      # 不存在的域返回nil值
```

## hkeys

**HKEYS key**

返回哈希表 `key` 中的所有域。

- **可用版本：**

  >= 2.0.0

- **时间复杂度：**

  O(N)， `N` 为哈希表的大小。

- **返回值：**

  一个包含哈希表中所有域的表。当 `key` 不存在时，返回一个空表。

```
# 哈希表非空

redis> HMSET website google www.google.com yahoo www.yahoo.com
OK

redis> HKEYS website
1) "google"
2) "yahoo"


# 空哈希表/key不存在

redis> EXISTS fake_key
(integer) 0

redis> HKEYS fake_key
(empty list or set)
```

## hvals

**HVALS key**

返回哈希表 `key` 中所有域的值。

- **可用版本：**

  >= 2.0.0

- **时间复杂度：**

  O(N)， `N` 为哈希表的大小。

- **返回值：**

  一个包含哈希表中所有值的表。当 `key` 不存在时，返回一个空表。

```
# 非空哈希表

redis> HMSET website google www.google.com yahoo www.yahoo.com
OK

redis> HVALS website
1) "www.google.com"
2) "www.yahoo.com"


# 空哈希表/不存在的key

redis> EXISTS not_exists
(integer) 0

redis> HVALS not_exists
(empty list or set)
```

## hgetall

**HGETALL key**

返回哈希表 `key` 中，所有的域和值。

在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。

- **可用版本：**

  >= 2.0.0

- **时间复杂度：**

  O(N)， `N` 为哈希表的大小。

- **返回值：**

  以列表形式返回哈希表的域和域的值。若 `key` 不存在，返回空列表。

```
redis> HSET people jack "Jack Sparrow"
(integer) 1

redis> HSET people gump "Forrest Gump"
(integer) 1

redis> HGETALL people
1) "jack"          # 域
2) "Jack Sparrow"  # 值
3) "gump"
4) "Forrest Gump"
```