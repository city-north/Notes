# Read Uncommitted 读未提交

[TOC]

Read Uncommitted是隔离级别最低的一种事务级别。

在这种隔离级别下，一个事务会读到另一个事务更新后但未提交的数据，如果另一个事务回滚，那么当前事务读到的数据就是脏数据，这就是脏读（Dirty Read),引发的问题有:

- [脏读](06-脏读.md) 
- [不可重复读](07-不可重复读.md) 
- [幻读](08-幻读.md) 

## 读未提交-代码实例

##### 设置本次查询 session 的隔离级别为 Read Uncommited

```sql
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
```

##### 表结构

```sql
DROP TABLE IF EXISTS account;
CREATE TABLE `account` (
`id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT,
`user_name` VARCHAR ( 36 ) NOT NULL,
`balance` BIGINT ( 20 ) NOT NULL,
PRIMARY KEY ( `id` ),
UNIQUE KEY `id` ( `id` ) 
) ENGINE = INNODB AUTO_INCREMENT = 8 DEFAULT CHARSET = utf8;

INSERT INTO account (user_name,balance) values ('张三',100);
INSERT INTO account (user_name,balance) values ('李四', 0);

```

##### SESSION 开启事务

```sql
-- SESSION 1
START TRANSACTION;
SELECT * FROM ACCOUNT;
--  张三转账
UPDATE account SET balance=balance-100 WHERE user_name='张三';
```

在 SESSION 1 没有提交的情况下 SESSION2 查看

```
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
SELECT * FROM ACCOUNT; -- SESSION2
```

在读未提交的隔离级别下,`SESSION2` 依然可以读到`SESSION1`未提交的更改,这就是读未提交

## 会出现的问题

|      | 隔离级别                                              | 脏读   | 不可重复读 | 幻读          |
| ---- | ----------------------------------------------------- | ------ | ---------- | ------------- |
| 1    | [读未提交（read uncommitted)](01-read-uncommitted.md) | 脏读   | 可能       | 可能          |
| 2    | [读已提交（read committed)](01-read-uncommitted.md)   | 不可能 | 可能       | 可能          |
| 3    | [可重复读（repeatable read)](03-repeatable-read.md)   | 不可能 | 不可能     | InnoDB 不可能 |
| 4    | [串行化 (serializable)](04-serializable.md)           | 不可能 | 不可能     | 不可能        |

