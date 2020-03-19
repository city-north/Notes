# PERSIST 命令

Redis PERSIST 命令用于移除给定 key 的过期时间，使得 key 永不过期。

### 语法

redis PERSIST 命令基本语法如下：

```
redis 127.0.0.1:6379> PERSIST KEY_NAME
```

### 返回值

当过期时间移除成功时，返回 1 。 如果 key 不存在或 key 没有设置过期时间，返回 0 。

### 实例

```
redis> SET mykey "Hello"
OK

redis> EXPIRE mykey 10  # 为 key 设置生存时间
(integer) 1

redis> TTL mykey
(integer) 10

redis> PERSIST mykey    # 移除 key 的生存时间
(integer) 1

redis> TTL mykey
(integer) -1
```