# Setbit 命令

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