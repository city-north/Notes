# Redis Ltrim 命令

Redis Ltrim 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。

下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。

### 语法

redis Ltrim 命令基本语法如下：

```
redis 127.0.0.1:6379> LTRIM KEY_NAME START STOP
```

### 可用版本

\>= 1.0.0

### 返回值

命令执行成功时，返回 ok 。

### 实例

```
redis 127.0.0.1:6379> RPUSH mylist "hello"
(integer) 1
redis 127.0.0.1:6379> RPUSH mylist "hello"
(integer) 2
redis 127.0.0.1:6379> RPUSH mylist "foo"
(integer) 3
redis 127.0.0.1:6379> RPUSH mylist "bar"
(integer) 4
redis 127.0.0.1:6379> LTRIM mylist 1 -1
OK
redis 127.0.0.1:6379> LRANGE mylist 0 -1
1) "hello"
2) "foo"
3) "bar"
```