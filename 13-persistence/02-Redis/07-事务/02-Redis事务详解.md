# Redis 事务

[TOC]

## 为什么要用事务

我们知道 Redis 的单个命令是原子性的(比如 get set mget mset)，如果涉及到 多个命令的时候，需要把多个命令作为一个不可分割的处理序列，就需要用到事务。

例如我们之前说的用 setnx 实现分布式锁，我们先 set，然后设置对 key 设置 expire， 防止 del 发生异常的时候锁不会被释放，业务处理完了以后再 del，这三个动作我们就希 望它们作为一组命令执行。

Redis 的事务有两个特点: 

- 按进入队列的顺序执行。

- 不会受到其他客户端的请求的影响。

Redis 的事务涉及到四个命令:multi(开启事务)，exec(执行事务)，discard (取消事务)，watch(监视)

## 事务的用法

```
127.0.0.1:6379> set tom 1000 OK
127.0.0.1:6379> set mic 1000 OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> decrby tom 100 QUEUED
127.0.0.1:6379> incrby mic 100 QUEUED
127.0.0.1:6379> exec
4
1) (integer) 900
2) (integer) 1100 127.0.0.1:6379> get tom "900"
127.0.0.1:6379> get mic "1100"
```

通过 multi 的命令开启事务。

事务不能嵌套，多个 multi 命令效果一样。

multi 执行后，客户端可以继续向服务器发送任意多条命令， 这些命令不会立即被 执行， 而是被放到一个队列中， 当 exec 命令被调用时， 所有队列中的命令才会被执 行。

通过 exec 的命令执行事务。如果没有执行 exec，所有的命令都不会被执行。 如果中途不想执行事务了，怎么办?

可以调用 discard 可以清空事务队列，放弃执行。

```
multi 
set k1 1 
set k2 2 
set k3 3 
discard
```

#### watch命令

> 实际上是一种乐观锁机制

在 Redis 中还提供了一个 watch 命令。
它可以为 Redis 事务提供 CAS 乐观锁行为(Check and Set / Compare and Swap)，

也就是多个线程更新变量的时候，会跟原值做比较，只有它没有被其他线程修 改的情况下，才更新成新的值。

我们可以用 watch **监视一个或者多个 key**，如果开启事务之后，至少有一个被监视 key 键在 exec 执行之前被修改了， 那么整个事务都会被取消(key 提前过期除外)。可以用 unwatch 取消。

| client 1                                                     | client 2                                         |
| ------------------------------------------------------------ | ------------------------------------------------ |
| 127.0.0.1:6379> set balance 1000 OK<br/>127.0.0.1:6379> watch balance OK<br/>127.0.0.1:6379> multi<br/>OK<br/>127.0.0.1:6379> incrby balance 100 QUEUED |                                                  |
|                                                              | 127.0.0.1:6379> decrby balance 100 (integer) 900 |
| 127.0.0.1:6379> exec<br/>(nil)<br/>127.0.0.1:6379> get balance "900" |                                                  |

#### 事务可能遇到的问题

我们把事务执行遇到的问题分成两种，一种是在执行 exec 之前发生错误，一种是在 执行 exec 之后发生错误。

- exec 之前发生错误, 在这种情况下事务会被拒绝执行，也就是队列中所有的命令都不会得到执行。
- exec 之后发生错误, 只有错误的命令没有被执行，但是其他命令没有受到影响。

这个显然不符合我们对原子性的定义，也就是我们没办法用 Redis 的这种事务机制 来实现原子性，保证数据的一致。LUA 脚本

## 为什么Redis不支持回滚（rollback）

> https://blog.csdn.net/yangshangwei/article/details/82866216?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param

如果你有使用关系式数据库的经验， 那么 “Redis 在事务失败时不进行回滚，而是继续执行余下的命令”这种做法可能会让你觉得有点奇怪。

以下是这种做法的优点：

> - redis只有在语法错误而回滚，Redis作者认为语法错误不应该发生在生产上面
> - 因为不需要对回滚进行支持，所以 Redis 的内部可以保持简单且快速。

- Redis 命令只会因为错误的语法而失败（并且这些问题不能在入队时发现），或是命令用在了错误类型的键上面：这也就是说，从实用性的角度来说，失败的命令是由编程错误造成的，而这些错误应该在开发的过程中被发现，而不应该出现在生产环境中。
- 有种观点认为 Redis 处理事务的做法会产生 bug ， 然而需要注意的是，

 在通常情况下， 回滚并不能解决编程错误带来的问题。 举个例子， 如果你本来想通过 [INCR key](http://redisdoc.com/string/incr.html#incr) 命令将键的值加上 `1` ， 却不小心加上了 `2` ， 又或者对错误类型的键执行了 [INCR key](http://redisdoc.com/string/incr.html#incr) ， 回滚是没有办法处理这些情况的。

鉴于没有任何机制能避免程序员自己造成的错误， 并且这类错误通常不会在生产环境中出现， 所以 Redis 选择了更简单、更快速的无回滚方式来处理事务。

## 为什么Redis不支持语法错误回滚

对于 Redis 而言，不单单需要注意其事务处理的过程，其回滚的能力也和数据库不太一样

- 命令格式正确而数据类型不符合
- 命令格式错误

#### 命令格式正确而数据类型不符合

```java
127.0.0.1:6379> FLUSHDB
OK
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> SET key1 value1
QUEUED
127.0.0.1:6379> SET key2 value2
QUEUED
127.0.0.1:6379> INCR key1
QUEUED
127.0.0.1:6379> DEL key2
QUEUED
127.0.0.1:6379> EXEC
1) OK
2) OK
3) (error) ERR value is not an integer or out of range
4) (integer) 1
127.0.0.1:6379> GET key1
"value1"
127.0.0.1:6379> GET key2
(nil)
127.0.0.1:6379> 

1234567891011121314151617181920212223
```

我们将 key1 设置为字符串，而使用命令 incr 对其自增，但是命令只会进入事务队列，而没有被执行，所以它不会有任何的错误发生，而是等待 exec 命令的执行。

当 exec 命令执行后，之前进入队列的命令就依次执行，当遇到 incr 时发生命令操作的数据类型错误，所以显示出了错误，而其之前和之后的命令都会被正常执行.

------

## 命令格式错误

注意，这里命令格式是正确的，问题在于数据类型，对于**命令格式是错误的却是另外一种情形** 如下所示

```java
127.0.0.1:6379> FLUSHDB
OK
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set key1 value1
QUEUED
127.0.0.1:6379> incr
(error) ERR wrong number of arguments for 'incr' command
127.0.0.1:6379> set key2 value2
QUEUED
127.0.0.1:6379> EXEC
(error) EXECABORT Transaction discarded because of previous errors.
127.0.0.1:6379> GET key1
(nil)
127.0.0.1:6379> GET key2
(nil)
127.0.0.1:6379> 


12345678910111213141516171819
```

可以看到我们使用的 incr 命令格式是错误的，这个时候 Redis 会立即检测出来并产生错误，而在此之前我们设置了 keyl ， 在此之后我们设置了 key2 a 当事务执行的时候，我们发现 keyl 和 key2 的值都为空，说明被 Redis 事务回滚了。

------

## 总结

通过上面两个例子，可以看出Redis在执行事务命令的时候，在命令入队的时候， Redis 就会检测事务的**命令是否正确**，如果**不正确**则会产生错误。无论**之前和之后的命令都会被事务所回滚**，就变为什么都没有执行。

当**命令格式正确**，而因为**操作数据结构引起的错误** ，则该命令执行出现错误，而**其之前和之后的命令都会被正常执行**。这点和数据库很不一样，这是需注意的地方。

对于一些重要的操作，我们必须通过程序去检测数据的正确性，以保证 Redis 事务的正确执行，避免出现数据不一致的情况。 Redis 之所以保持这样简易的事务，完全是为了保证移动互联网的核心问题一----性能。

