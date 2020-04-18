# Redis Llen 命令

Redis Llen 命令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。

### 语法

redis Llen 命令基本语法如下：

```
redis 127.0.0.1:6379> LLEN KEY_NAME 
```

### 可用版本

\>= 1.0.0

### 返回值

列表的长度。

### 实例

```
redis 127.0.0.1:6379> RPUSH list1 "foo"
(integer) 1
redis 127.0.0.1:6379> RPUSH list1 "bar"
(integer) 2
redis 127.0.0.1:6379> LLEN list1
(integer) 2
```