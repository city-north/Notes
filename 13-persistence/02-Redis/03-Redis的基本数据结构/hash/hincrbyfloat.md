# Redis Hincrbyfloat 命令

Redis Hincrbyfloat 命令用于为哈希表中的字段值加上指定浮点数增量值。



如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。

### 语法

redis Hincrbyfloat 命令基本语法如下：

```
HINCRBYFLOAT key field increment
```

### 可用版本

\>= 2.6.0

### 返回值

执行 Hincrbyfloat 命令之后，哈希表中字段的值。

### 实例

```
redis> HSET mykey field 10.50
(integer) 1
redis> HINCRBYFLOAT mykey field 0.1
"10.6"
redis> HINCRBYFLOAT mykey field -5
"5.6"
redis> HSET mykey field 5.0e3
(integer) 0
redis> HINCRBYFLOAT mykey field 2.0e2
"5200"
redis> 
```