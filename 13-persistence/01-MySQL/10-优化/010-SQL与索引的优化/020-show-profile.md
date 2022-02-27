# 020-show-profile

[TOC]

## 简介

> https://dev.mysql.com/doc/refman/5.7/en/show-profile.html

SHOW PROFILE 是谷歌高级架构师 Jeremy Cole 贡献给 MySQL 社区的，可以查看SQL 语句执行的时候使用的资源，比如 CPU、IO 的消耗情况。 在 SQL 中输入 help profile 可以得到详细的帮助信息。

#### 查看是否开启

```sql
select @@profiling; 
set @@profiling=1; // SESSION 级别开启 prifile 
```

#### 查看 profile 统计

```
show profiles; --(命令最后带一个 s)
```

| Query\_ID | Duration | Query                                                        |
| :-------- | :------- | :----------------------------------------------------------- |
| 9         | 0.000048 | SHOW WARNINGS                                                |
| 10        | 0.000297 | /\* ApplicationName=DataGrip 2020.2 \*/ select database\(\)  |
| 11        | 0.00025  | SHOW WARNINGS                                                |
| 12        | 0.000046 | SHOW WARNINGS                                                |
| 13        | 0.00005  | /\* ApplicationName=DataGrip 2020.2 \*/ SET net\_write\_timeout=600 |
| 14        | 0.00003  | /\* ApplicationName=DataGrip 2020.2 \*/ SET SQL\_SELECT\_LIMIT=501 |

查看最后一个 SQL 的执行详细信息，从中找出耗时较多的环节(没有 s)。

```
show profile;
```

查询结果

| Status         | Duration |
| :------------- | :------- |
| starting       | 0.000048 |
| Opening tables | 0.000007 |
| query end      | 0.000003 |
| closing tables | 0.000002 |
| freeing items  | 0.000011 |
| cleaning up    | 0.000007 |

## 查看每个状态消耗的事件

也可以根据 ID 查看执行详细信息，在后面带上 for query + ID。

```
show profile for query 1;
```







除了慢日志和 show profile，如果要分析出当前数据库中执行的慢的 SQL，还可以通过查看运行线程状态和服务器运行信息、存储引擎信息来分析。







