# Redis Hkeys 命令

Redis Hkeys 命令用于获取哈希表中的所有域（field）。



### 语法

redis Hkeys 命令基本语法如下：

```
redis 127.0.0.1:6379> HKEYS key 
```

### 可用版本

\>= 2.0.0

### 返回值

包含哈希表中所有域（field）列表。 当 key 不存在时，返回一个空列表。

### 实例

```
redis 127.0.0.1:6379> HSET myhash field1 "foo"
(integer) 1
redis 127.0.0.1:6379> HSET myhash field2 "bar"
(integer) 1
redis 127.0.0.1:6379> HKEYS myhash
1) "field1"
2) "field2"
```