# Redis 的基本数据类型

> http://redisdoc.com/index.html

- **string 字符串**
- **list 列表**
- **hash 字典**
- **set 集合**
- **zset 有序集合**

## String 字符串

### 操作

#### MSET

设置多个值

```
127.0.0.1:6379> mset firstname max nextname Chen
```

#### SETNX

判断是否存在 key, 存在即跳过;不存在即设置

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

基于此方法可以实现分布式锁,使用 del key 释放锁

但是如果释放锁的操作失败了,导致其他节点永远获取不到锁怎么办?

加过期时间。单独用 expire 加过期, 也失败了,无法保证原子性,怎么办? 多参数

### SET

```
set key value [expiration EX seconds|PX milliseconds][NX|XX]
```

使用参数的方式

```
set lock1 1 EX 10 NX
```

- `EX seconds` ： 将键的过期时间设置为 `seconds` 秒。 执行 `SET key value EX seconds` 的效果等同于执行 `SETEX key seconds value` 。
- `PX milliseconds` ： 将键的过期时间设置为 `milliseconds` 毫秒。 执行 `SET key value PX milliseconds` 的效果等同于执行 `PSETEX key milliseconds value` 。
- `NX` ： 只在键不存在时， 才对键进行设置操作。 执行 `SET key value NX` 的效果等同于执行 `SETNX key value` 。
- `XX` ： 只在键已经存在时， 才对键进行设置操作。