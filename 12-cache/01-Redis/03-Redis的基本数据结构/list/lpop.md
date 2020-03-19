# Redis Lpop 命令

Redis Lpop 命令用于移除并返回列表的第一个元素。

### 语法

redis Lpop 命令基本语法如下：

```
redis 127.0.0.1:6379> Lpop KEY_NAME 
```

### 可用版本

\>= 1.0.0

### 返回值

列表的第一个元素。 当列表 key 不存在时，返回 nil 。

### 实例

```
redis 127.0.0.1:6379> RPUSH list1 "foo"
(integer) 1
redis 127.0.0.1:6379> RPUSH list1 "bar"
(integer) 2
redis 127.0.0.1:6379> LPOP list1
"foo"
```