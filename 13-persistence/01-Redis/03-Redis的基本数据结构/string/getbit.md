# Getbit 命令

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