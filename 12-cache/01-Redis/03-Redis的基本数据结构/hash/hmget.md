# Redis Hmget 命令

Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。

如果指定的字段不存在于哈希表，那么返回一个 nil 值。

### 语法

redis Hmget 命令基本语法如下：

```
redis 127.0.0.1:6379> HMGET KEY_NAME FIELD1...FIELDN 
```

### 可用版本

\>= 2.0.0

### 返回值

一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。

### 实例

```
redis 127.0.0.1:6379> HSET myhash field1 "foo"
(integer) 1
redis 127.0.0.1:6379> HSET myhash field2 "bar"
(integer) 1
redis 127.0.0.1:6379> HMGET myhash field1 field2 nofield
1) "foo"
2) "bar"
3) (nil)
```