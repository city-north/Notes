# 030-Redis-key通用操作.md

[TOC]

## 简介

Redis 键命令用于管理 redis 的键。

## 语法

Redis 键命令的基本语法如下：

```
redis 127.0.0.1:6379> COMMAND KEY_NAME
```

## 实例

```
redis 127.0.0.1:6379> SET runoobkey redis
OK
redis 127.0.0.1:6379> DEL runoobkey
(integer) 1
```

在以上实例中 **DEL** 是一个命令， **runoobkey** 是一个键。 如果键被删除成功，命令执行后输出 **(integer) 1**，否则将输出 **(integer) 0**

## Redis key 命令

| 序号 | 命令      | 描述                                                         | 实例                              |
| ---- | --------- | ------------------------------------------------------------ | --------------------------------- |
| 1    | del       | 用于在 key 存在时删除 key                                    | `del name`                        |
| 2    | dump      | 序列化给定 key,并返回被序列化的值                            | `dump greeting`                   |
| 3    | exist     | 检查给定的 key 是否存在                                      | `exist name`                      |
| 4    | expire    | 为给定的 key 设置过期时间,以秒计算                           | `expire name 1000`                |
| 5    | pexpire   | 设置 key的过期时间,以毫秒计算                                | `pexpire key 1000`                |
| 6    | pexpireat | 设置 key 的过期时间的时间戳,以毫秒计算                       | `SET runoobkey redis`             |
| 7    | keys      | 查找所有符合给定模式 pattern 的 key                          | `keys apple*`                     |
| 8    | move      | 将数据库中的 key 移动到给定的数据库 db 中                    |                                   |
| 9    | presist   | 移除 key 的过期时间,key 将持久保持                           |                                   |
| 10   | pttl      | 以毫秒为单位返回 key 的剩余的过期时间                        |                                   |
| 11   | ttl       | 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。 | [ttl.md](keys/ttl.md)             |
| 12   | randomkey | 从当前数据库中随机返回一个 key                               | [randomkey.md](keys/randomkey.md) |
| 13   | rename    | 修改 key 的名称                                              | [rename.md](keys/rename.md)       |
| 14   | renamenx  | 仅当 newkey 不存在时，将 key 改名为 newkey 。                | [renameex.md](keys/renameex.md)   |
| 15   | type      | 返回 key 所存储的值的类型                                    | [type.md](keys/type.md)           |

#### DEL key 删除

用于在 key 存在时删除 key

```
redis 127.0.0.1:6379> SET w3ckey redis
OK
```

现在我们删除已创建的 key。

```
redis 127.0.0.1:6379> DEL w3ckey
(integer) 1
```

#### DUMP key

**序列化给定 key , 并返回被序列化的值**

首先，我们在 redis 中创建一个 key 并设置值。

```
redis> SET greeting "hello, dumping world!"
OK
```

现在使用 DUMP 序列化键值。

```
redis> DUMP greeting
"\x00\x15hello, dumping world!\x06\x00E\xa0Z\x82\xd8r\xc1\xde"

redis> DUMP not-exists-key
(nil)
```

#### EXIST key

检查给定 key 是否存在。若 key 存在返回 1 ，否则返回 0 。

#### EXPIRE key seconds

Redis Expire 命令用于设置 key 的过期时间，key 过期后将不再可用。单位以秒计。

设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。

首先创建一个 key 并赋值：

```
redis 127.0.0.1:6379> SET runooobkey redis
OK
```

为 key 设置过期时间：

```
redis 127.0.0.1:6379> EXPIRE runooobkey 60
(integer) 1
```

以上实例中我们为键 runooobkey 设置了过期时间为 1 分钟，1分钟后该键会自动删除。

#### Expireat 命令

Redis Expireat 命令用于以 UNIX 时间戳(unix timestamp)格式设置 key 的过期时间。key 过期后将不再可用。

设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。

##### 实例

首先创建一个 key 并赋值：

```
redis 127.0.0.1:6379> SET runoobkey redis
OK
```

为 key 设置过期时间：

```
redis 127.0.0.1:6379> EXPIREAT runoobkey 1293840000
(integer) 1
redis 127.0.0.1:6379> EXISTS runoobkey
(integer) 0
```

