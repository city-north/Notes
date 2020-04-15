# getset

Redis Getset 命令用于设置指定 key 的值，并返回 key 的旧值。

### 语法

redis Getset 命令基本语法如下：

```
redis 127.0.0.1:6379> GETSET KEY_NAME VALUE
```

### 返回值

返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 nil 。

当 key 存在但不是字符串类型时，返回一个错误。

### 实例

首先，设置 mykey 的值并截取字符串。

```
redis> GETSET db mongodb    # 没有旧值，返回 nil
(nil)

redis> GET db
"mongodb"

redis> GETSET db redis      # 返回旧值 mongodb
"mongodb"

redis> GET db
"redis"
```

