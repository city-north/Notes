# Redis Hlen 命令



Redis Hlen 命令用于获取哈希表中字段的数量。

### 语法

redis Hlen 命令基本语法如下：

```
redis 127.0.0.1:6379> HLEN KEY_NAME 
```

### 可用版本

\>= 2.0.0

### 返回值

哈希表中字段的数量。 当 key 不存在时，返回 0 。

### 实例

```
redis 127.0.0.1:6379> HSET myhash field1 "foo"
(integer) 1
redis 127.0.0.1:6379> HSET myhash field2 "bar"
(integer) 1
redis 127.0.0.1:6379> HLEN myhash
(integer) 2
```