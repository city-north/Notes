# Redis Decrby 命令

Redis Decrby 命令将 key 所储存的值减去指定的减量值。

如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。

如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

本操作的值限制在 64 位(bit)有符号数字表示之内。

### 语法

redis Decrby 命令基本语法如下：

```
redis 127.0.0.1:6379> DECRBY KEY_NAME DECREMENT_AMOUNT
```

### 可用版本

\>= 1.0.0

### 返回值

减去指定减量值之后， key 的值。

### 实例

```
# 对已存在的 key 进行 DECRBY

redis> SET count 100
OK

redis> DECRBY count 20
(integer) 80


# 对不存在的 key 进行DECRBY

redis> EXISTS pages
(integer) 0

redis> DECRBY pages 10
(integer) -10
```