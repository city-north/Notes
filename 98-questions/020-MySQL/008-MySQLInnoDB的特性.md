# [QA]MySQLInnoDB的特性

> InnoDB的体系结构

首先 MySQL的体系结构是分为 server 层和 存储引擎层, 存储引擎层才有可插拔的插件形式 ,InnoDB 就是一个插件

三个方向介绍 InnoDB

- 内存
- 线程
- 磁盘

## 内存

- changeBuffer
- dataBuffer
- indexBuffer
- redoLogBuffer
- doubleWriteBuffer

## s内存刷新到磁盘的机制

- redoLogbuffer的刷新条件
- 脏页的刷新条件
- binlogcache的刷新条件

## 各种线程的作用

- masterThread
- purgeThread
- redoLogThread
- readThread
- writeThread
- pageCleanerThread

## 磁盘中存放的文件

- redoLog
- undoLog
- binLog

| 序号 | 特性       | 实现方式                                                     |
| ---- | ---------- | ------------------------------------------------------------ |
| 1    | 支持事务   | 1. 通过 UndoLog 实现原子性<br />2. 通过 MVCC 和 LBCC 来实现隔离性<br />3. 通过 redoLog 和 doubleWrite机制 实现持久性 |
| 2    | 访问速度快 | 1. 通过聚集索引3 次 IO访问 2000w 条数据<br />2. 通过缓冲池提高查询速度,通过 ChangeBuffer刷脏机制 提高更新效率 |
| 3    | 查询效率快 | 特殊的索引存放方式，可以减少 IO，提升查询效率。              |
| 4    | 读写并发   | ,写不阻塞读(MVCC) 来获得高并发性,使用  **next-key-locking** 的策略避免幻读 **(phantom)**  现象的产生 |

