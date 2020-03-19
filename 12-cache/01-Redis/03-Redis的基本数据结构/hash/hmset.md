# Redis Hmset 命令

Redis Hmset 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。

此命令会覆盖哈希表中已存在的字段。

如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。

### 语法

redis Hmset 命令基本语法如下：

```
redis 127.0.0.1:6379> HMSET KEY_NAME FIELD1 VALUE1 ...FIELDN VALUEN  
```

### 可用版本

\>= 2.0.0

### 返回值

如果命令执行成功，返回 OK 。

### 实例

```
redis 127.0.0.1:6379> HMSET myhash field1 "Hello" field2 "World"
OK
redis 127.0.0.1:6379> HGET myhash field1
"Hello"
redis 127.0.0.1:6379> HGET myhash field2
"World"
```