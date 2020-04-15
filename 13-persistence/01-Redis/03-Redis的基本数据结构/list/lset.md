# Redis Lset 命令

Redis Lset 通过索引来设置元素的值。

当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。

关于列表下标的更多信息，请参考 [LINDEX 命令](https://www.runoob.com/redis/lists-lindex.html)。

### 语法

redis Lset 命令基本语法如下：

```
redis 127.0.0.1:6379> LSET KEY_NAME INDEX VALUE
```

### 可用版本

\>= 1.0.0

### 返回值

操作成功返回 ok ，否则返回错误信息。

### 实例

```
redis 127.0.0.1:6379> RPUSH mylist "hello"
(integer) 1
redis 127.0.0.1:6379> RPUSH mylist "hello"
(integer) 2
redis 127.0.0.1:6379> RPUSH mylist "foo"
(integer) 3
redis 127.0.0.1:6379> RPUSH mylist "hello"
(integer) 4
redis 127.0.0.1:6379> LSET mylist 0 "bar"
OK
redis 127.0.0.1:6379> LRANGE mylist 0 -1
1: "bar"
2) "hello"
3) "foo"
4) "hello"
```