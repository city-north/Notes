# Redis Msetnx 命令

Redis Msetnx 命令用于所有给定 key 都不存在时，同时设置一个或多个 key-value 对。

### 语法

redis Msetnx 命令基本语法如下：

```
redis 127.0.0.1:6379> MSETNX key1 value1 key2 value2 .. keyN valueN 
```

### 可用版本

\>= 1.0.1

### 返回值

当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。

### 实例

```
# 对不存在的 key 进行 MSETNX

redis> MSETNX rmdbs "MySQL" nosql "MongoDB" key-value-store "redis"
(integer) 1

redis> MGET rmdbs nosql key-value-store
1) "MySQL"
2) "MongoDB"
3) "redis"


# MSET 的给定 key 当中有已存在的 key

redis> MSETNX rmdbs "Sqlite" language "python"  # rmdbs 键已经存在，操作失败
(integer) 0

redis> EXISTS language                          # 因为 MSET 是原子性操作，language 没有被设置
(integer) 0

redis> GET rmdbs                                # rmdbs 也没有被修改
"MySQL"
```