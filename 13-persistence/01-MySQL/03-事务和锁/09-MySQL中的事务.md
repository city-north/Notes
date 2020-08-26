# MySQL 中的事务

- 什么是数据库事务
- MySQL InnoDB 锁的基本类型
- 行锁的原理
- 锁的算法
- 事务隔离

## 什么是事务

 [README.md](../../../08-transaction/01-database-transaction/README.md) 

## 数据库什么时候会出现事务

无论是我们在 Navicat 的这种工具里面去操作，还是在我们的 Java 代码里面通过 API 去操作，还是加上`@Transactional `的注解或者 AOP 配置，其实最终都是发送一个 指令到数据库去执行，Java 的 JDBC 只不过是把这些命令封装起来了。

我们先来看一下我们的操作环境。版本(5.7)，存储引擎(InnnoDB)，事务隔离 级别(RR)。

```
select version(); -- 版本(5.7)
show variables like '%engine%'; -- 存储引擎(InnnoDB)
show global variables like "tx_isolation"; --事务隔离 级别(RR)。
```

### 自动提交

InnoDB 里面有一个 autocommit 的参数(分成两个级别， session 级别和 global级别)。

```
show variables like 'autocommit';
```

它的默认值是 ON。autocommit 这个参数是什么意思呢?是否自动提交。如果它的 值是 true/on 的话，我们在操作数据的时候，会自动开启一个事务，和自动提交事务。

否则，如果我们把 autocommit 设置成 false/off，那么数据库的事务就需要我们手 动地去开启和手动地去结束。

#### 手动开启事务

有几种方式，一种是用 begin;一种是用 start transaction。

#### 结束事务

- commit;

- rollback，

- 客户端 的连接断开的时候，事务也会结束。

## 事务的隔离级别会出现的问题

|      | 隔离级别                                                     | [脏读](../../../08-transaction/01-database-transaction/06-脏读.md) | [不可重复读](../../../08-transaction/01-database-transaction/07-不可重复读.md) | [幻读](../../../08-transaction/01-database-transaction/08-幻读.md) |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1    | [读未提交（read uncommitted](../../../08-transaction/01-database-transaction/01-read-uncommitted.md) | 可能                                                         | 可能                                                         | 可能                                                         |
| 2    | [读已提交（read committed)](../../../08-transaction/01-database-transaction/02-read-committed.md) | 不可能                                                       | 可能                                                         | 可能                                                         |
| 3    | [可重复读（repeatable read)](../../../08-transaction/01-database-transaction/03-repeatable-read.md) | 不可能                                                       | 不可能                                                       | InnoDB 不可能                                                |
| 4    | [串行化 (serializable)](../../../08-transaction/01-database-transaction/04-serializable.md) | 不可能                                                       | 不可能                                                       | 不可能                                                       |

InnoDB 可以解决可重复读问题

## 实现方案

-   [LBCC](011-LBCC-基于锁的并发控制.md) 
-  [MVCC](010-MVCC-多版本并发控制.md) 

## InnoDB是如何实现事务的

InnoDB存储引擎对ACID的实现方式
利用回滚日志（undo log） 和 重做日志（redo log） 两种表实现事务，并实现 MVCC (多版本并发控制)；

在执行事务的每条SQL时，会先将数据原值写入undo log 中， 然后执行SQL对数据进行修改，最后将修改后的值写入redo log中。

redo log 重做日志包括两部分：1 是内存中的重做日志缓冲 ；2 是重做日志文件。在事务提交时，必须先将该事务的所有日志写入到重做日志文件进行持久化，待事务commit操作完成才算完成。

当一个事务中的所有SQL都执行成功后，会将redo log 缓存中的数据刷入磁盘，然后提交。

| 特征        | INNODB实现方式                                   |
| ----------- | ------------------------------------------------ |
| 原子性（A） | 回滚日志（undo log）：用于记录数据修改前的状态； |
| 一致性（C） | 重做日志（redo log）：用于记录数据修改后的状态； |
| 隔离性（I） | 锁：用于资源隔离，分为共享锁和排它锁；           |
| 持久性（D） | 重做日志（redo log） + 回滚日志（undo log）；    |

