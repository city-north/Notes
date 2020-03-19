# Redis Hset 命令

Redis Hset 命令用于为哈希表中的字段赋值 。

如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。

如果字段已经存在于哈希表中，旧值将被覆盖。

### 语法

redis Hset 命令基本语法如下：

```
redis 127.0.0.1:6379> HSET KEY_NAME FIELD VALUE 
```

### 可用版本

\>= 2.0.0

### 返回值

如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。

### 实例

```
redis 127.0.0.1:6379> HSET myhash field1 "foo"
OK
redis 127.0.0.1:6379> HGET myhash field1
"foo"

redis 127.0.0.1:6379> HSET website google "www.g.cn"       # 设置一个新域
(integer) 1

redis 127.0.0.1:6379>HSET website google "www.google.com" # 覆盖一个旧域
(integer) 0
```