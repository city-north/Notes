# 010-通过show-status命令了解各种执行频率

[TOC]

## 简介

MySQL 链接成功后, 通过 `show [session| global] status` 命令可以提供服务器状态信息, 也可以根据需要加上参数 session 或者 global 来显示 session(当前链接)的统计结果和 global 级(自数据库上次启动至今) 的统计结果, 默认是 session 级别

#### 统计命令执行频率

```
show status like 'Com_%'
```

| Variable\_name | Value |                                                           |
| :------------- | :---- | --------------------------------------------------------- |
| Com\_select    | 40    | 执行 select 操作的次数, 一次查询只累加 1                  |
| Com_insert     | 0     | 执行 insert的语句, 对于批量插入的 insert 操作, 只累加一次 |
| Com_update     | 1     | 执行 update 操作的次数                                    |
| Com_delete     | 0     | 执行 delete 操作的次数                                    |

```sql
show status like 'innodb_rows_%'
```

| Variable\_name         | Value |
| :--------------------- | :---- |
| Innodb\_rows\_deleted  | 0     |
| Innodb\_rows\_inserted | 0     |
| Innodb\_rows\_read     | 0     |
| Innodb\_rows\_updated  | 0     |

- 可以很容易地了解到当前数据库的应是插入更细为主还是以查询操作为主, 以及各种类型的 SQL 大致执行的比例是多少, 

- 对于更新操作的技术, 是对执行次数的计数, 不论提交还是回滚都会进行累加

对于事务型的应用:

通过 `Com_commit` 和 `Com_rollback`可以了解事务提交和回滚的情况, 对于回滚操作非常频繁的数据库, 可能意味着编写存在问题

- Connections : 试图链接 MySQL 服务器的次数
- Uptime: 服务器工作时间
- Slow_queries: 慢查询的次数
- 