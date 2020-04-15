# Redis Setex 命令

Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。

### 语法

redis Setex 命令基本语法如下：

```
redis 127.0.0.1:6379> SETEX KEY_NAME TIMEOUT VALUE
```

### 可用版本

\>= 2.0.0

### 返回值

设置成功时返回 OK 。

### 实例

```
redis 127.0.0.1:6379> SETEX mykey 60 redis
OK
redis 127.0.0.1:6379> TTL mykey
60
redis 127.0.0.1:6379> GET mykey
"redis
```