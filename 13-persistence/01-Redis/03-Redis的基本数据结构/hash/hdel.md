# Redis Hdel 命令

Redis Hdel 命令用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。

### 语法

redis Hdel 命令基本语法如下：

```
redis 127.0.0.1:6379> HDEL KEY_NAME FIELD1.. FIELDN 
```

### 可用版本

\>= 2.0.0

### 返回值

被成功删除字段的数量，不包括被忽略的字段。

### 实例

```
redis 127.0.0.1:6379> HSET myhash field1 "foo"
(integer) 1
redis 127.0.0.1:6379> HDEL myhash field1
(integer) 1
redis 127.0.0.1:6379> HDEL myhash field2
(integer) 0
```