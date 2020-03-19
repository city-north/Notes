# Hgetall 命令

Redis Hgetall 命令用于返回哈希表中，所有的字段和值。

在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍。

### 语法

redis Hgetall 命令基本语法如下：

```
redis 127.0.0.1:6379> HGETALL KEY_NAME 
```

### 可用版本

\>= 2.0.0

### 返回值

以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。

### 实例

```
redis> HSET myhash field1 "Hello"
(integer) 1
redis> HSET myhash field2 "World"
(integer) 1
redis> HGETALL myhash
1) "field1"
2) "Hello"
3) "field2"
4) "World"
redis> 
```