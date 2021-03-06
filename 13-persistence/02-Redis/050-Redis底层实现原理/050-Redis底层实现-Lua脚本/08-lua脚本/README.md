# Lua 脚本

Lua/ˈluə/是一种轻量级脚本语言，它是用 C 语言编写的，跟数据的存储过程有点类 似。 使用 Lua 脚本来执行 Redis 命令的好处:

- 一次发送多个命令，减少网络开销。
- Redis 会将整个脚本作为一个整体执行，不会被其他请求打断，保持原子性。

- 对于复杂的组合命令，我们可以放在文件中，可以实现程序之间的命令集复用。

### 在Redis中调用Lua脚本

使用 eval /ɪ'væl/ 方法，语法格式:

```
redis> eval lua-script key-num [key1 key2 key3 ....] [value1 value2 value3 ....]
```

- eval代表执行Lua语言的命令。
- lua-script代表Lua语言脚本内容。
- key-num表示参数中有多少个key，需要注意的是Redis中key是从1开始的，如果没有key的参数，那么写0。 
- [key1key2key3...]是key作为参数传递给Lua语言，也可以不填，但是需要和key-num的个数对应起来。
- [value1 value2 value3 ....]这些参数传递给 Lua 语言，它们是可填可不填的。

示例，返回一个字符串，0 个参数:

```
127.0.0.1:6379> eval "return  'hello Wrold '" 0 
"hello Wrold "
```

### 在Lua脚本中调用Redis命令

使用 `redis.call(command, key [param1, param2...]) `进行操作。语法格式:

```
redis> eval "redis.call('set',KEYS[1],ARGV[1])" 1 lua-key lua-value
```

- command是命令，包括set、get、del等。 
- key是被操作的键。
- param1,param2...代表给key的参数。

注意跟 Java 不一样，定义只有形参，调用只有实参。
Lua 是在调用时用 key 表示形参，argv 表示参数值(实参)。

## 设置键值对

在 Redis 中调用 Lua 脚本执行 Redis 命令

```
redis> eval "return redis.call('set',KEYS[1],ARGV[1])" 1 gupao 2673 
redis> get gupao
```

以上命令等价于 set gupao 2673。

在 redis-cli 中直接写 Lua 脚本不够方便，也不能实现编辑和复用，通常我们会把脚 本放在文件里面，然后执行这个文件。

创建 Lua 脚本文件:

```
cd /usr/local/soft/redis5.0.5/src vim gupao.lua
```

Lua 脚本内容，先设置，再取值

```java
redis.call('set','gupao','lua666') 
return redis.call('get','gupao')
```

在 Redis 客户端中调用 Lua 脚本

```
cd /usr/local/soft/redis5.0.5/src 
redis-cli --eval gupao.lua 0
```

得到返回值:

```
[root@localhost src]# redis-cli --eval gupao.lua 0 
"lua666"
```

## 案例:对 IP 进行限流 

[13-限流.md](../03-Redis的基本数据结构/13-限流.md) 