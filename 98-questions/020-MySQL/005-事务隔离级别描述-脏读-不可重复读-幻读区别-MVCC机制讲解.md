# 事务隔离级别描述-脏读-不可重复读-幻读区别-MVCC机制讲解

|      | 隔离级别                                              | 脏读   | 不可重复读 | 幻读          |
| ---- | ----------------------------------------------------- | ------ | ---------- | ------------- |
| 1    | [读未提交（read uncommitted)](01-read-uncommitted.md) | 脏读   | 可能       | 可能          |
| 2    | [读已提交（read committed)](01-read-uncommitted.md)   | 不可能 | 可能       | 可能          |
| 3    | [可重复读（repeatable read)](03-repeatable-read.md)   | 不可能 | 不可能     | InnoDB 不可能 |
| 4    | [串行化 (serializable)](04-serializable.md)           | 不可能 | 不可能     | 不可能        |

### 为什么InnoDB在RR级别中不会出现幻读

- 间隙锁

### MVCC是什么