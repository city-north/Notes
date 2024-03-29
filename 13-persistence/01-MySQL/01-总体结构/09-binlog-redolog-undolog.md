# BinLog 和 RedoLog 和 UndoLog

[TOC]

## 简介

- BinLog 二进制日志 , 在 Server 层实现, 所有的存储引擎都会使用到

可以理解为存储的 sql 语句和其逆向的语句,用于主从复制和数据库基于时间点还原

- RedoLog 重做日志, 在 数据引擎层实现. InnoDB 中

确保事务的持久性。
　　防止在发生故障的时间点，尚有脏页未写入磁盘，在重启mysql服务的时候，根据redo log进行重做，从而达到事务的持久性这一特性

- Undolog

**撤销日志, 主要记录了事务发生之前的数据状态,如果修改数据的时候发生异常,可以用 undo log 实现回滚**

##   redo log

> redo log 又称重做日志文件, 用于记录事务操作的变化,记录的是数据修改之后的值,不管事务是否提交都会被记录下来,. 当数据库掉电这种极端情况发生时,Innodb 会使用重做日志恢复到掉电前的状态,来确保数据的完整性

- 作用

确保事务的持久性。防止在发生故障的时间点，尚有脏页未写入磁盘，在重启mysql服务的时候，根据redo log进行重做，从而达到事务的持久性这一特性。

- 内容：

物理格式的日志，记录的是物理数据页面的修改的信息，其redo log是顺序写入redo log file的物理文件中去的。

- 什么时候产生

事务开始之后就产生redo log，redo log的落盘并不是随着事务的提交才写入的，而是在事务的执行过程中，便开始写入redo log文件中。

- 什么时候释放：

当对应事务的脏页写入到磁盘之后，redo log的使命也就完成了，重做日志占用的空间就可以重用（被覆盖）。

- 对应的物理文件

　　默认情况下，对应的物理文件位于数据库的data目录下的ib_logfile1&ib_logfile2

```java
innodb_log_group_home_dir 指定日志文件组所在的路径，默认./ ，表示在数据库的数据目录下。
innodb_log_files_in_group 指定重做日志文件组中文件的数量，默认2关于文件的大小和数量，由一下两个参数配置
innodb_log_file_size 重做日志文件的大小。
innodb_mirrored_log_groups 指定了日志镜像文件组的数量，默认1
```

- redo log 是什么时候开始写盘的

事务开始之后逐步写盘的,之所以说重做日志是在事务开始之后逐步写入重做日志文件，而不一定是事务提交才写入重做日志缓存，原因就是，重做日志有一个缓存区 **Innodb_log_buffer**，**Innodb_log_buffer** 的默认大小为8M,Innodb存储引擎先将重做日志写入**innodb_log_buffer**中,然后会通过以下三种方式将innodb日志缓冲区的日志刷新到磁盘:

> 1，Master Thread 每秒一次执行刷新Innodb_log_buffer到重做日志文件。
> 2，每个事务提交时会将重做日志刷新到重做日志文件。
> 3，当重做日志缓存可用空间 少于一半时，重做日志缓存被刷新到重做日志文件



由此可以看出，重做日志通过不止一种方式写入到磁盘，尤其是对于第一种方式，Innodb_log_buffer到重做日志文件是Master Thread线程的定时任务。因此重做日志的写盘，并不一定是随着事务的提交才写入重做日志文件的，而是随着事务的开始，逐步开始的。
　　另外引用《MySQL技术内幕 Innodb 存储引擎》（page37）上的原话：

> 即使某个事务还没有提交，Innodb存储引擎仍然每秒会将重做日志缓存刷新到重做日志文件。
> 这一点是必须要知道的，因为这可以很好地解释再大的事务的提交（commit）的时间也是很短暂的。

## **UndoLog 回滚日志**

- 作用

保存了事务发生之前的数据的一个版本，可以用于回滚，同时可以提供多版本并发控制下的读（MVCC），也即非锁定读

> undo log和redo log记录物理日志不一样，它是逻辑日志。**可以认为当delete一条记录时，undo log中会记录一条对应的insert记录，反之亦然，当update一条记录时，它记录一条对应相反的update记录。**

- 内容

逻辑格式的日志，在执行undo的时候，仅仅是将数据从逻辑上恢复至事务之前的状态，而不是从物理页面上操作实现的，这一点是不同于redo log的。

- 什么时候产生的

事务开始之前，将当前的版本生成undo log，undo 也会产生 redo 来保证undo log的可靠性

- 什么时候开始释放

当事务提交之后, undo log 并不能立马被删除,而是放入待清理的链表,由 pruge 线程判断是否有其他事务在使用 undo 段中标的上一个事务之前的版本信息,决定是否清理 undo log

- 对应的物理文件

　　MySQL5.6之前，undo表空间位于共享表空间的回滚段中，共享表空间的默认的名称是ibdata，位于数据文件目录中。

　　MySQL5.6之后，undo表空间可以配置成独立的文件，但是提前需要在配置文件中配置，完成数据库初始化后生效且不可改变undo log文件的个数

　　如果初始化数据库之前没有进行相关配置，那么就无法配置成独立的表空间了。

> 关于MySQL5.7之后的独立undo 表空间配置参数如下
> 　　innodb_undo_directory = /data/undospace/ --undo独立表空间的存放目录
> 　　innodb_undo_logs = 128 --回滚段为128KB
> 　　innodb_undo_tablespaces = 4 --指定有4个undo log文件

如果undo使用的共享表空间，这个共享表空间中又不仅仅是存储了undo的信息，共享表空间的默认为与MySQL的数据目录下面，其属性由参数innodb_data_file_path配置。

> ![img](../../../assets/180128104819882.png)

- 其他

　undo是在事务开始之前保存的被修改数据的一个版本，产生undo日志的时候，同样会伴随类似于保护事务持久化机制的redolog的产生。

　　默认情况下undo文件是保持在共享表空间的，也即ibdatafile文件中，当数据库中发生一些大的事务性操作的时候，要生成大量的undo信息，全部保存在共享表空间中的。

　　因此共享表空间可能会变的很大，默认情况下，也就是undo 日志使用共享表空间的时候，被“撑大”的共享表空间是不会也不能自动收缩的。

　　因此，mysql5.7之后的“ 独立undo 表空间”的配置就显得很有必要了。

## BinLog 二进制日志

- 作用
  - 用于复制, 主从复制中,从库利用主库中的 binLog 进行复制,实现主从复制
  - 用于数据库的基于时间点的还原

- 内容

逻辑格式的日志，可以简单认为就是执行过的事务中的sql语句。但又不完全是sql语句这么简单，而是附加执行的sql语句（增删改）反向的信息，

- 也就意味着delete对应着delete本身和其反向的insert；
- update对应着update执行前后的版本的信息；
- insert对应着delete和insert本身的信息。

> 在使用mysqlbinlog解析binlog之后一些都会真相大白。
> 因此可以基于binlog做到类似于[Oracle](https://www.linuxidc.com/topicnews.aspx?tid=12)的闪回功能，其实都是依赖于binlog中的日志记录。

- 什么时候产生

事务提交的时候，一次性将事务中的sql语句（一个事物可能对应多个sql语句）按照一定的格式记录到binlog中。

> ​		这里与redo log很明显的差异就是redo log并不一定是在事务提交的时候刷新到磁盘，redo log是在事务开始之后就开始逐步写入磁盘。
> 　　因此对于事务的提交，即便是较大的事务，提交（commit）都是很快的，但是在开启了bin_log的情况下，对于较大事务的提交，可能会变得比较慢一些。
> 　　这是因为binlog是在事务提交的时候一次性写入的造成的，这些可以通过测试验证。

- 什么时候释放

　　binlog的默认是保持时间由参数expire_logs_days配置，也就是说对于非活动的日志文件，在生成时间超过expire_logs_days配置的天数之后，会被自动删除。

> ![img](../../../assets/180128104819883.png)

- 对应的物理文件

> 　　配置文件的路径为log_bin_basename，binlog日志文件按照指定大小，当日志文件达到指定的最大的大小之后，进行滚动更新，生成新的日志文件。
> 　　对于每个binlog日志文件，通过一个统一的index文件来组织。
>
> ![img](../../../assets/180128104819884.png)

## redolog 和 binlog 的区别

1. 记录时间不同
   - binlog 是逻辑日志, 记录所有数据的改变信息
   - redolog 是物理日志,记录所有 InnoDb 表数据的变化
2. 记录内容不同
   1. binlog 记录commit 完毕之后的 DML 和 DDL SQL 语句
   2. redolog 记录的是事务发起之后的 DML 和 DDL SQL 语句
3. 文件使用方式不同
   1. binlog 不能循环使用,写满或者实例重启,会生成新的 binlog
   2. redolog 可以循环使用,最后一个文件写满之后,会重新写第一个文件
4. 作用不同
   1. binlog 可以用来恢复数据使用,主从复制
   2. redolog 可以异常宕机或者介质故障后的数据恢复使用

#### 问题: 如何保证 InnodDB 存储引擎的日志文件(redolog undolog)和二进制文件保持一致呢

主从环境中,从库需要通过二进制文件来应用主库提交的事务,但是如果

主库 redo log 已经提交而 binlog 没有保持一致,那么就会造成从库数据就是,主从数据不一致的情况

所以需要两阶段提交协议

- 准备阶段: 事务 SQL 语句先写入 redo log buffer ,然后做一个事务标记准备,再将 logbuffer中的数据刷新到 redolog
- 提交阶段: 将事务产生的 binlog 写入文件,刷入磁盘

然后在 redolog 中做一个事务提交的标记,并把 binlog 写成功的也一并写入 redolog 文件

## 三个 log 的时序图

![image-20200313212639117](../../../assets/image-20200313212639117-7629638.png)

例如一条语句:update teacher set name='盆鱼宴' where id=1;

- 先查询到这条数据，如果有缓存，也会用到缓存。
- 把 name 改成盆鱼宴，然后调用引擎的 API 接口，写入这一行数据到内存，同时
- 记录 redo log。这时 redo log 进入 prepare 状态，然后告诉执行器，执行完成了，可 以随时提交。
- 执行器收到通知后记录 binlog，然后调用存储引擎接口，设置 redo log 为 commit 状态。
- 更新完成。

这张图片的重点:

- 先记录到内存，再写日志文件。
- 记录 redo log 分为两个阶段。
- 存储引擎和 Server 记录不同的日志。 
- 先记录 redo，再记录 binlog