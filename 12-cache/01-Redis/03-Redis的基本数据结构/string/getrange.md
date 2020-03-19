# getrange

Redis Getrange 命令用于获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。

### 语法

redis Getrange 命令基本语法如下：

```
redis 127.0.0.1:6379> GETRANGE KEY_NAME start end
```

### 返回值

截取得到的子字符串。

### 实例

首先，设置 mykey 的值并截取字符串。

```
redis 127.0.0.1:6379> SET mykey "This is my test key"
OK
redis 127.0.0.1:6379> GETRANGE mykey 0 3
"This"
redis 127.0.0.1:6379> GETRANGE mykey 0 -1
"This is my test key"
```