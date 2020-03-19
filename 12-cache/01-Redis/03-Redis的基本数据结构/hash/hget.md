# Redis Hget 命令

Redis Hget 命令用于返回哈希表中指定字段的值。

### 语法

redis Hget 命令基本语法如下：

```
redis 127.0.0.1:6379> HGET KEY_NAME FIELD_NAME 
```

### 可用版本

\>= 2.0.0

### 返回值

返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil 。

### 实例

```
# 字段存在

redis> HSET site redis redis.com
(integer) 1

redis> HGET site redis
"redis.com"


# 字段不存在

redis> HGET site mysql
(nil)
```