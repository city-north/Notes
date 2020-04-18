# Redis Lindex 命令

Redis Lindex 命令用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。

### 语法

redis Lindex 命令基本语法如下：

```
redis 127.0.0.1:6379> LINDEX KEY_NAME INDEX_POSITION 
```

### 可用版本

\>= 1.0.0

### 返回值

列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。

### 实例

```
redis 127.0.0.1:6379> LPUSH mylist "World"
(integer) 1

redis 127.0.0.1:6379> LPUSH mylist "Hello"
(integer) 2

redis 127.0.0.1:6379> LINDEX mylist 0
"Hello"

redis 127.0.0.1:6379> LINDEX mylist -1
"World"

redis 127.0.0.1:6379> LINDEX mylist 3        # index不在 mylist 的区间范围内
(nil)
```