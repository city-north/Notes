# Incr 命令

Redis Incr 命令将 key 中储存的数字值增一。

如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。

如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

本操作的值限制在 64 位(bit)有符号数字表示之内。

### 语法

redis Incr 命令基本语法如下：

```
redis 127.0.0.1:6379> INCR KEY_NAME 
```

### 可用版本

\>= 1.0.0

### 返回值

执行 INCR 命令之后 key 的值。

### 实例

```
redis> SET page_view 20
OK

redis> INCR page_view
(integer) 21

redis> GET page_view    # 数字值在 Redis 中以字符串的形式保存
"21"
```