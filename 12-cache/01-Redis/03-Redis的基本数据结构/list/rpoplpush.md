# Redis Rpoplpush 命令

Redis Rpoplpush 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。

### 语法

redis Rpoplpush 命令基本语法如下：

```
redis 127.0.0.1:6379> RPOPLPUSH SOURCE_KEY_NAME DESTINATION_KEY_NAME
```

### 可用版本

\>= 1.0.0

### 返回值

被弹出的元素。

### 实例

```
redis 127.0.0.1:6379> RPUSH mylist "hello"
(integer) 1
redis 127.0.0.1:6379> RPUSH mylist "foo"
(integer) 2
redis 127.0.0.1:6379> RPUSH mylist "bar"
(integer) 3
redis 127.0.0.1:6379> RPOPLPUSH mylist myotherlist
"bar"
redis 127.0.0.1:6379> LRANGE mylist 0 -1
1) "hello"
2) "foo"
```