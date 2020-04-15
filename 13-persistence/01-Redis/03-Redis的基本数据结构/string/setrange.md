# SetRange

Redis Setrange 命令用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。

### 语法

redis Setrange 命令基本语法如下：

```
redis 127.0.0.1:6379> SETRANGE KEY_NAME OFFSET VALUE
```

### 可用版本

\>= 2.2.0

### 返回值

被修改后的字符串长度。

### 实例

```
redis 127.0.0.1:6379> SET key1 "Hello World"
OK
redis 127.0.0.1:6379> SETRANGE key1 6 "Redis"
(integer) 11
redis 127.0.0.1:6379> GET key1
"Hello Redis"
```