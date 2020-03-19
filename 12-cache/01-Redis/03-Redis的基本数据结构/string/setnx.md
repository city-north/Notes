# Setnx 命令

Redis Setnx（**SET** if **N**ot e**X**ists） 命令在指定的 key 不存在时，为 key 设置指定的值。

### 语法

redis Setnx 命令基本语法如下：

```
redis 127.0.0.1:6379> SETNX KEY_NAME VALUE
```

### 可用版本

\>= 1.0.0

### 返回值

设置成功，返回 1 。 设置失败，返回 0 。

### 实例

```
redis> EXISTS job                # job 不存在
(integer) 0

redis> SETNX job "programmer"    # job 设置成功
(integer) 1

redis> SETNX job "code-farmer"   # 尝试覆盖 job ，失败
(integer) 0

redis> GET job                   # 没有被覆盖
"programmer"
```