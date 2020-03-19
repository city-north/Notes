# Psetex 命令

Redis Psetex 命令以毫秒为单位设置 key 的生存时间。

### 语法

redis Psetex 命令基本语法如下：

```
redis 127.0.0.1:6379> PSETEX key1 EXPIRY_IN_MILLISECONDS value1 
```

### 可用版本

\>= 2.6.0

### 返回值

设置成功时返回 OK 。

### 实例

```
redis 127.0.0.1:6379> PSETEX mykey 1000 "Hello"
OK
redis 127.0.0.1:6379> PTTL mykey
999
redis 127.0.0.1:6379> GET mykey
1) "Hello"
```