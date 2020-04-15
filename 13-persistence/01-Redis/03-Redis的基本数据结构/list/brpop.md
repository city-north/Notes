# Redis Brpop 命令

Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

### 语法

redis Brpop 命令基本语法如下：

```
redis 127.0.0.1:6379> BRPOP LIST1 LIST2 .. LISTN TIMEOUT 
```

### 可用版本

\>= 2.0.0

### 返回值

假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。

### 实例

```
redis> DEL list1 list2
(integer) 0
redis> RPUSH list1 a b c
(integer) 3
redis> BRPOP list1 list2 0
1) "list1"
2) "c"
```