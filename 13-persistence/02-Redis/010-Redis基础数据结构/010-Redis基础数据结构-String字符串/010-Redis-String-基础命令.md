# 010-Redis-String-基础命令

[TOC]

| 序号 | 命令                  | 描述                                                         |
| ---- | --------------------- | ------------------------------------------------------------ |
| 1    | [set](#set)           | 指定 key 的值                                                |
| 2    | [get](#get)           | 获取指定 key的值                                             |
| 3    | [getrange](#getrange) | 返回 key 中字符的子字符                                      |
| 4    | [getset](#getset)     | 将给定的key 的值设为 value,并返回 key 的旧值                 |
| 5    | [getbit](#getbit)     | 对 key 所存储字符串值,获取指定偏移量上的位                   |
| 6    | [mget](#mget)         | 获取所有一个或多个给定 key 的值                              |
| 7    | [setbit](#setbit)     | 对 key 所储存的字符串量,设置或清除指定偏移量上的位           |
| 8    | [setex](#setex)       | 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。 |
| 9    | [setnx](#setnx)       | 只有在 key 不存的设置 key的值                                |
| 10   | [setrange](#setrange) | 用 value 参数复写给定 key 所储存的字符串值,从偏移量 offset开始 |
| 11   | [strlen](#strlen)     | 返回 key 所储存的字符串值的长度。                            |
| 12   | [mset](#mset)         | 同时设置一个或多个 key-value 对。                            |
| 13   | [msetnx](#msetnx)     | 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。 |
| 14   | [psetex](#psetex)     | 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。 |
| 15   | [incr](#incr)         | 将 key 中储存的数字值增一                                    |
| 16   | [Incrby](#Incrby)     | 将 key 所储存的值加上给定的增量值（increment）               |
| 17   | [incrbyfloat](#)      | 将 key 所储存的值加上给定的浮点增量值（increment）           |
| 18   | [decr](#decr)         | 将 key 中储存的数字值减一                                    |
| 19   | [decrby](#decrby)     | key 所储存的值减去给定的减量值（decrement)                   |
| 20   | [append](#append)     | 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。 |

## set

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(1)

将字符串值 `value` 关联到 `key` 。

如果 `key` 已经持有其他值， `SET` 就覆写旧值， 无视类型。

当 `SET` 命令对一个带有生存时间（TTL）的键进行设置之后， 该键原有的 TTL 将被清除。

### 可选参数

从 Redis 2.6.12 版本开始， `SET` 命令的行为可以通过一系列参数来修改：

- `EX seconds` ： 将键的过期时间设置为 `seconds` 秒。 执行 `SET key value EX seconds` 的效果等同于执行 `SETEX key seconds value` 。
- `PX milliseconds` ： 将键的过期时间设置为 `milliseconds` 毫秒。 执行 `SET key value PX milliseconds` 的效果等同于执行 `PSETEX key milliseconds value` 。
- `NX` ： 只在键不存在时， 才对键进行设置操作。 执行 `SET key value NX` 的效果等同于执行 `SETNX key value` 。
- `XX` ： 只在键已经存在时， 才对键进行设置操作。

Note

因为 `SET` 命令可以通过参数来实现 `SETNX` 、 `SETEX` 以及 `PSETEX` 命令的效果， 所以 Redis 将来的版本可能会移除并废弃 `SETNX` 、 `SETEX` 和 `PSETEX` 这三个命令。

### 返回值

在 Redis 2.6.12 版本以前， `SET` 命令总是返回 `OK` 。

从 Redis 2.6.12 版本开始， `SET` 命令只在设置操作成功完成时才返回 `OK` ； 如果命令使用了 `NX` 或者 `XX` 选项， 但是因为条件没达到而造成设置操作未执行， 那么命令将返回空批量回复（NULL Bulk Reply）。

### 代码示例

对不存在的键进行设置：

```
redis> SET key "value"
OK

redis> GET key
"value"
```

对已存在的键进行设置：

```
redis> SET key "new-value"
OK

redis> GET key
"new-value"
```

使用 `EX` 选项：

```
redis> SET key-with-expire-time "hello" EX 10086
OK

redis> GET key-with-expire-time
"hello"

redis> TTL key-with-expire-time
(integer) 10069
```

使用 `PX` 选项：

```
redis> SET key-with-pexpire-time "moto" PX 123321
OK

redis> GET key-with-pexpire-time
"moto"

redis> PTTL key-with-pexpire-time
(integer) 111939
```

使用 `NX` 选项：

```
redis> SET not-exists-key "value" NX
OK      # 键不存在，设置成功

redis> GET not-exists-key
"value"

redis> SET not-exists-key "new-value" NX
(nil)   # 键已经存在，设置失败

redis> GEt not-exists-key
"value" # 维持原值不变
```

使用 `XX` 选项：

```
redis> EXISTS exists-key
(integer) 0

redis> SET exists-key "value" XX
(nil)   # 因为键不存在，设置失败

redis> SET exists-key "value"
OK      # 先给键设置一个值

redis> SET exists-key "new-value" XX
OK      # 设置新值成功

redis> GET exists-key
"new-value"
```

## get

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(1)

返回与键 `key` 相关联的字符串值。

### 返回值

如果键 `key` 不存在， 那么返回特殊值 `nil` ； 否则， 返回键 `key` 的值。

如果键 `key` 的值并非字符串类型， 那么返回一个错误， 因为 `GET` 命令只能用于字符串值。

### 代码示例

对不存在的键 `key` 或是字符串类型的键 `key` 执行 `GET` 命令：

```
redis> GET db
(nil)

redis> SET db redis
OK

redis> GET db
"redis"
```

对不是字符串类型的键 `key` 执行 `GET` 命令：

```
redis> DEL db
(integer) 1

redis> LPUSH db redis mongodb mysql
(integer) 3

redis> GET db
(error) ERR Operation against a key holding the wrong kind of value
```

## getrange

Redis Getrange 命令用于获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。

### 语法

redis Getrange 命令基本语法如下：

```shell
redis 127.0.0.1:6379> GETRANGE KEY_NAME start end
```

### 返回值

截取得到的子字符串。

### 实例

首先，设置 mykey 的值并截取字符串。

```shell
redis 127.0.0.1:6379> SET mykey "This is my test key"
OK
redis 127.0.0.1:6379> GETRANGE mykey 0 3
"This"
redis 127.0.0.1:6379> GETRANGE mykey 0 -1
"This is my test key"
```

## getset

Redis Getset 命令用于设置指定 key 的值，并返回 key 的旧值。

### 语法

redis Getset 命令基本语法如下：

```
redis 127.0.0.1:6379> GETSET KEY_NAME VALUE
```

### 返回值

返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 nil 。

当 key 存在但不是字符串类型时，返回一个错误。

### 实例

首先，设置 mykey 的值并截取字符串。

```shell
redis> GETSET db mongodb    # 没有旧值，返回 nil
(nil)

redis> GET db
"mongodb"

redis> GETSET db redis      # 返回旧值 mongodb
"mongodb"

redis> GET db
"redis"
```

## getbit

Redis Getbit 命令用于对 key 所储存的字符串值，获取指定偏移量上的位(bit)。

### 语法

redis Getbit 命令基本语法如下：

```
redis 127.0.0.1:6379> GETBIT KEY_NAME OFFSET
```

### 返回值

字符串值指定偏移量上的位(bit)。

当偏移量 OFFSET 比字符串值的长度大，或者 key 不存在时，返回 0 。

### 实例

```
# 对不存在的 key 或者不存在的 offset 进行 GETBIT， 返回 0

redis> EXISTS bit
(integer) 0

redis> GETBIT bit 10086
(integer) 0


# 对已存在的 offset 进行 GETBIT

redis> SETBIT bit 10086 1
(integer) 0

redis> GETBIT bit 10086
(integer) 1
```

## mget

Redis Mget 命令返回所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。

### 语法

redis Mget 命令基本语法如下：

```
redis 127.0.0.1:6379> MGET KEY1 KEY2 .. KEYN
```

### 实例

```
redis 127.0.0.1:6379> SET key1 "hello"
OK
redis 127.0.0.1:6379> SET key2 "world"
OK
redis 127.0.0.1:6379> MGET key1 key2 someOtherKey
1) "Hello"
2) "World"
3) (nil)
```

## setbit

Redis Setbit 命令用于对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。

### 语法

redis Setbit 命令基本语法如下：

```
redis 127.0.0.1:6379> Setbit KEY_NAME OFFSET
```

### 返回值

指定偏移量原来储存的位。

### 实例

```
redis> SETBIT bit 10086 1
(integer) 0

redis> GETBIT bit 10086
(integer) 1

redis> GETBIT bit 100   # bit 默认被初始化为 0
(integer) 0
```

## setex

Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。

### 语法

redis Setex 命令基本语法如下：

```
redis 127.0.0.1:6379> SETEX KEY_NAME TIMEOUT VALUE
```

### 可用版本

\>= 2.0.0

### 返回值

设置成功时返回 OK 。

### 实例

```
redis 127.0.0.1:6379> SETEX mykey 60 redis
OK
redis 127.0.0.1:6379> TTL mykey
60
redis 127.0.0.1:6379> GET mykey
"redis
```

## setnx

Redis Setnx（**SET** if **N**ot e**X**ists） 命令在指定的 key 不存在时，为 key 设置指定的值。

### 语法

redis Setnx 命令基本语法如下：

```
redis 127.0.0.1:6379> SETNX KEY_NAME VALUE
```

### 可用版本

\>= 1.0.0

### 返回值

设置成功，返回 1 。 设置失败，返回 0 。

### 实例

```
redis> EXISTS job                # job 不存在
(integer) 0

redis> SETNX job "programmer"    # job 设置成功
(integer) 1

redis> SETNX job "code-farmer"   # 尝试覆盖 job ，失败
(integer) 0

redis> GET job                   # 没有被覆盖
"programmer"
```

## setrange

Redis Setrange 命令用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。

### 语法

redis Setrange 命令基本语法如下：

```
redis 127.0.0.1:6379> SETRANGE KEY_NAME OFFSET VALUE
```

### 可用版本

\>= 2.2.0

### 返回值

被修改后的字符串长度。

### 实例

```
redis 127.0.0.1:6379> SET key1 "Hello World"
OK
redis 127.0.0.1:6379> SETRANGE key1 6 "Redis"
(integer) 11
redis 127.0.0.1:6379> GET key1
"Hello Redis"
```

## strlen

Redis Strlen 命令用于获取指定 key 所储存的字符串值的长度。当 key 储存的不是字符串值时，返回一个错误。

### 语法

redis Strlen 命令基本语法如下：

```
redis 127.0.0.1:6379> STRLEN KEY_NAME
```

### 可用版本

\>= 2.2.0

### 返回值

字符串值的长度。 当 key 不存在时，返回 0。

### 实例

```
# 获取字符串的长度

redis> SET mykey "Hello world"
OK

redis> STRLEN mykey
(integer) 11


# 不存在的 key 长度为 0

redis> STRLEN nonexisting
(integer) 0
```

## mset

> 可用版本： >= 1.0.1
>
> 时间复杂度： O(N)，其中 N 为被设置的键数量。

同时为多个键设置值。

如果某个给定键已经存在， 那么 `MSET` 将使用新值去覆盖旧值， 如果这不是你所希望的效果， 请考虑使用 `MSETNX` 命令， 这个命令只会在所有给定键都不存在的情况下进行设置。

`MSET` 是一个原子性(atomic)操作， 所有给定键都会在同一时间内被设置， 不会出现某些键被设置了但是另一些键没有被设置的情况。

### 返回值

`MSET` 命令总是返回 `OK` 。

### 代码示例

同时对多个键进行设置：

```
redis> MSET date "2012.3.30" time "11:00 a.m." weather "sunny"
OK

redis> MGET date time weather
1) "2012.3.30"
2) "11:00 a.m."
3) "sunny"
```

覆盖已有的值：

```
redis> MGET k1 k2
1) "hello"
2) "world"

redis> MSET k1 "good" k2 "bye"
OK

redis> MGET k1 k2
1) "good"
2) "bye"
```

## psetex

> 可用版本： >= 2.6.0
>
> 时间复杂度： O(1)

这个命令和 `SETEX` 命令相似， 但它以毫秒为单位设置 `key` 的生存时间， 而不是像 `SETEX` 命令那样以秒为单位进行设置。

#### 返回值

命令在设置成功时返回 `OK` 。

#### 代码示例

```
redis> PSETEX mykey 1000 "Hello"
OK

redis> PTTL mykey
(integer) 999

redis> GET mykey
"Hello"
```

## incr

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(1)

为键 `key` 储存的数字值加上一。

如果键 `key` 不存在， 那么它的值会先被初始化为 `0` ， 然后再执行 `INCR` 命令。

如果键 `key` 储存的值不能被解释为数字， 那么 `INCR` 命令将返回一个错误。

本操作的值限制在 64 位(bit)有符号数字表示之内。

Note

`INCR` 命令是一个针对字符串的操作。 因为 Redis 并没有专用的整数类型， 所以键 `key` 储存的值在执行 `INCR` 命令时会被解释为十进制 64 位有符号整数。

### 返回值

`INCR` 命令会返回键 `key` 在执行加一操作之后的值。

### 代码示例

```
redis> SET page_view 20
OK

redis> INCR page_view
(integer) 21

redis> GET page_view    # 数字值在 Redis 中以字符串的形式保存
"21"
```

## Incrby

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(1)

将键 `key` 储存的整数值减去减量 `decrement` 。

如果键 `key` 不存在， 那么键 `key` 的值会先被初始化为 `0` ， 然后再执行 `DECRBY` 命令。

如果键 `key` 储存的值不能被解释为数字， 那么 `DECRBY` 命令将返回一个错误。

本操作的值限制在 64 位(bit)有符号数字表示之内。

关于更多递增(increment) / 递减(decrement)操作的更多信息， 请参见 `INCR` 命令的文档。

### 返回值

`DECRBY` 命令会返回键在执行减法操作之后的值。

### 代码示例

对已经存在的键执行 `DECRBY` 命令：

```
redis> SET count 100
OK

redis> DECRBY count 20
(integer) 80
```

对不存在的键执行 `DECRBY` 命令：

```
redis> EXISTS pages
(integer) 0

redis> DECRBY pages 10
(integer) -10
```

## incrbyfloat

> 可用版本： >= 2.6.0
>
> 时间复杂度： O(1)

为键 `key` 储存的值加上浮点数增量 `increment` 。

如果键 `key` 不存在， 那么 `INCRBYFLOAT` 会先将键 `key` 的值设为 `0` ， 然后再执行加法操作。

如果命令执行成功， 那么键 `key` 的值会被更新为执行加法计算之后的新值， 并且新值会以字符串的形式返回给调用者。

无论是键 `key` 的值还是增量 `increment` ， 都可以使用像 `2.0e7` 、 `3e5` 、 `90e-2` 那样的指数符号(exponential notation)来表示， 但是， **执行 INCRBYFLOAT 命令之后的值**总是以同样的形式储存， 也即是， 它们总是由一个数字， 一个（可选的）小数点和一个任意长度的小数部分组成（比如 `3.14` 、 `69.768` ，诸如此类)， 小数部分尾随的 `0` 会被移除， 如果可能的话， 命令还会将浮点数转换为整数（比如 `3.0` 会被保存成 `3` ）。

此外， 无论加法计算所得的浮点数的实际精度有多长， `INCRBYFLOAT` 命令的计算结果最多只保留小数点的后十七位。

当以下任意一个条件发生时， 命令返回一个错误：

- 键 `key` 的值不是字符串类型(因为 Redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）；
- 键 `key` 当前的值或者给定的增量 `increment` 不能被解释(parse)为双精度浮点数。

### 返回值

在加上增量 `increment` 之后， 键 `key` 的值。

### 代码示例

```
redis> GET decimal
"3.0"

redis> INCRBYFLOAT decimal 2.56
"5.56"

redis> GET decimal
"5.56"
```

## decr

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(1)

为键 `key` 储存的数字值减去一。

如果键 `key` 不存在， 那么键 `key` 的值会先被初始化为 `0` ， 然后再执行 `DECR` 操作。

如果键 `key` 储存的值不能被解释为数字， 那么 `DECR` 命令将返回一个错误。

本操作的值限制在 64 位(bit)有符号数字表示之内。

关于递增(increment) / 递减(decrement)操作的更多信息， 请参见 `INCR` 命令的文档。

### 返回值

`DECR` 命令会返回键 `key` 在执行减一操作之后的值。

### 代码示例

对储存数字值的键 `key` 执行 `DECR` 命令：

```
redis> SET failure_times 10
OK

redis> DECR failure_times
(integer) 9
```

对不存在的键执行 `DECR` 命令：

```
redis> EXISTS count
(integer) 0

redis> DECR count
(integer) -1
```

## decrby

> 可用版本： >= 1.0.0
>
> 时间复杂度： O(1)

将键 `key` 储存的整数值减去减量 `decrement` 。

如果键 `key` 不存在， 那么键 `key` 的值会先被初始化为 `0` ， 然后再执行 `DECRBY` 命令。

如果键 `key` 储存的值不能被解释为数字， 那么 `DECRBY` 命令将返回一个错误。

本操作的值限制在 64 位(bit)有符号数字表示之内。

关于更多递增(increment) / 递减(decrement)操作的更多信息， 请参见 `INCR` 命令的文档。

### 返回值

`DECRBY` 命令会返回键在执行减法操作之后的值。

### 代码示例

对已经存在的键执行 `DECRBY` 命令：

```
redis> SET count 100
OK

redis> DECRBY count 20
(integer) 80
```

对不存在的键执行 `DECRBY` 命令：

```
redis> EXISTS pages
(integer) 0

redis> DECRBY pages 10
(integer) -10
```

## append

> 可用版本： >= 2.0.0
>
> 时间复杂度： 平摊O(1)

如果键 `key` 已经存在并且它的值是一个字符串， `APPEND` 命令将把 `value` 追加到键 `key` 现有值的末尾。

如果 `key` 不存在， `APPEND` 就简单地将键 `key` 的值设为 `value` ， 就像执行 `SET key value` 一样。

### 返回值

追加 `value` 之后， 键 `key` 的值的长度。

### 示例代码

对不存在的 `key` 执行 `APPEND` ：

```
redis> EXISTS myphone               # 确保 myphone 不存在
(integer) 0

redis> APPEND myphone "nokia"       # 对不存在的 key 进行 APPEND ，等同于 SET myphone "nokia"
(integer) 5                         # 字符长度
```

对已存在的字符串进行 `APPEND` ：

```
redis> APPEND myphone " - 1110"     # 长度从 5 个字符增加到 12 个字符
(integer) 12

redis> GET myphone
"nokia - 1110"
```