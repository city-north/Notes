# Read Committed 读已提交

[TOC]

在Read Committed隔离级别下，一个事务可能会遇到不可重复读（Non Repeatable Read）的问题。

- [脏读](06-脏读.md) 
- [不可重复读](07-不可重复读.md) 
- [幻读](08-幻读.md) 

## 读已提交-代码实例

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

## 会出现的问题

|      | 隔离级别                                              | 脏读   | 不可重复读 | 幻读          |
| ---- | ----------------------------------------------------- | ------ | ---------- | ------------- |
| 1    | [读未提交（read uncommitted)](01-read-uncommitted.md) | 脏读   | 可能       | 可能          |
| 2    | [读已提交（read committed)](01-read-uncommitted.md)   | 不可能 | 可能       | 可能          |
| 3    | [可重复读（repeatable read)](03-repeatable-read.md)   | 不可能 | 不可能     | InnoDB 不可能 |
| 4    | [串行化 (serializable)](04-serializable.md)           | 不可能 | 不可能     | 不可能        |

## 不可重复读和幻读，的区别在那里呢?

不可重复读是修改或者删除，幻读是插入。

不可重复读是精确读

幻读是范围读的时候出现的问题

