# 040-通过show-profile 分析SQL

[TOC]

## 简介

MySQL 从 5.0.37 版本开始增加了对 show profiles 和 show profile 语句的支持, 通过 have_profiling 参数, 能够看到当前 MySQL 是否支持 profile

```
select @@have_profiling
```

 [020-show-profile.md](../../../../13-persistence/01-MySQL/10-优化/010-SQL与索引的优化/020-show-profile.md) 

## 示例

通过profile，我们能够更清楚地了解SQL执行的过程。例如，我们知道MyISAM表有表元数据的缓存（例如行数，即COUNT(*)值），那么对一个MyISAM表的COUNT(*)是不需要消耗太多资源的，而对于 InnoDB 来说，就没有这种元数据缓存，COUNT(*)执行得较慢。下面来做个实验验证一下。
首先，在一个innodb引擎的付款表payment上，执行一个COUNT(*)查询：

```sql
mysql> select count(*) from payment;
+----------+
| count(*) |
+----------+
| 16049 |
+----------+
```

1 row in set (0.01 sec)
执行完毕后，通过 show profiles语句，看到当前SQL的Query ID为 4：

```sql
mysql> show profiles;
+----------+------------+-------------------------------------+
| Query_ID | Duration | Query |
+----------+------------+-------------------------------------+
| 1 | 0.00019300 | SELECT DATABASE() |
| 2 | 0.00049000 | show databases|
| 3 | 0.00281600 | show tables |
| 4 | 0.00774175 | select count(*) from payment |
+----------+------------+-------------------------------------+
4 rows in set (0.00 sec)4 rows in set (0.00 sec)
```

通过 show profile for query 语句能够看到执行过程中线程的每个状态和消耗的时间：

```sql
mysql> show profile for query 4;
+--------------------------------+----------+
| Status | Duration |
+--------------------------------+----------+
| starting | 0.000026 |
| Waiting for query cache lock | 0.000006 |
| checking query cache for query | 0.000057 |
| checking permissions | 0.000011 |
| Opening tables | 0.000300 |
| System lock | 0.000016 |
| Waiting for query cache lock | 0.000024 |
| init | 0.000018 |
| optimizing | 0.000009 |
| statistics | 0.000016 |
| preparing | 0.000014 |
| executing| 0.000009 |
| Sending data| 0.007143 |
| end | 0.000011 || end | 0.000011 |
| query end | 0.000012 |
| closing tables| 0.000015 |
| freeing items| 0.000012 |
| Waiting for query cache lock | 0.000004 |
| freeing items| 0.000020 |
| Waiting for query cache lock | 0.000004 |
| freeing items| 0.000004 |
| storing result in query cache | 0.000006 |
| logging slow query | 0.000004 |
| cleaning up | 0.000005 |
+-----------------------------------+----------+
24 rows in set (0.00 sec)
```

注意：Sending data状态表示MySQL线程开始访问数据行并把结果返回给客户端，而不仅仅是返回结果给客户端。由于在Sending data状态下，MySQL线程往往需要做大量的磁盘读取操作，所以经常是整个查询中耗时最长的状态。

通过仔细检查 show profile for query的输出，能够发现在执行COUNT(*)的过程中，时间主要消耗在 Sending data 这个状态上。为了更清晰地看到排序结果，可以查询`INFORMATION_SCHEMA.PROFILING`表并按照时间做个DESC排序：

```sql
mysql> SET @query_id := 4;
Query OK, 0 rows affected (0.00 sec)
mysql> SELECT STATE, SUM(DURATION) AS Total_R,ROUND(->ROUND(100 * SUM(DURATION) /(SELECT SUM(DURATION) FROM INFORMATION_SCHEMA.PROFILING WHERE QUERY_ID = @query_id
), 2) AS Pct_R,
 COUNT(*) AS Calls,
 SUM(DURATION) / COUNT(*) AS "R/Call"
FROM INFORMATION_SCHEMA.PROFILING
WHERE QUERY_ID = @query_id
GROUP BY STATE
ORDER BY Total_R DESC;
+--------------------------------+----------+-------+-------+--------------+
| STATE | Total_R | Pct_R | Calls | R/Call |
+--------------------------------+----------+-------+-------+--------------+
| Sending data | 0.007143 | 92.22 | 1 | 0.0071430000 |
| Opening tables | 0.000300 | 3.87 | 1 | 0.0003000000 |
…
| logging slow query | 0.000004 | 0.05 | 1 | 0.0000040000 |
+--------------------------------+----------+-------+-------+--------------++--------------------------------+----------+-------+-------+--------------+
19 rows in set (0.04 sec)
```




在获取到最消耗时间的线程状态后，MySQL支持进一步选择 all、cpu、block io、context switch、page faults等明细类型来查看MySQL在使用什么资源上耗费了过高的时间，例如，选择查看CPU的耗费时间：

```sql
mysql> show profile cpu for query 4;
+--------------------------------+----------+----------+------------+
| Status | Duration | CPU_user | CPU_system |
+--------------------------------+----------+----------+------------+
| starting | 0.000036 | 0.000000 | 0.000000 |
…
| executing | 0.000009 | 0.000000 | 0.000000 |
| Sending data | 0.007143 | 0.006999 | 0.000000 |
| end | 0.000011 | 0.000000 | 0.000000 |
…
| logging slow query | 0.000002 | 0.000000 | 0.000000 |
| cleaning up | 0.000003 | 0.000000 | 0.000000 |
+--------------------------------+----------+----------+------------+
24 rows in set (0.00 sec)
```

能够发现Sending data状态下，时间主要消耗在CPU上了。
对比MyISAM表的COUNT(*)操作，也创建一个同样表结构的MyISAM表，数据量也完全一致：

```sql
mysql> create table payment_myisam like payment;mysql> show profile for query 10;
+--------------------------------+----------+
| Status | Duration |
+--------------------------------+----------+
| starting | 0.000029 |
…
| executing | 0.000015 |
| end| 0.000007 |
| query end| 0.000009 |
…
| cleaning up | 0.000006 |
+--------------------------------+----------+
21 rows in set (0.00 sec)
```

从 profile的结果能够看出，InnoDB引擎的表在COUNT(*)时经历了Sending data状态，存在访问数据的过程，而MyISAM引擎的表在executing之后直接就结束查询，完全不需要访问数据。
读者如果对MySQL源码感兴趣，还可以通过 show profile source for query查看SQL解析执行过程中每个步骤对应的源码的文件、函数名以及具体的源文件行数：

```sql
mysql> show profile source for query 4\G
…
*************************** 4. row ***************************Status: checking permissions
Duration: 0.000015
Source_function: check_access
Source_file: sql_parse.cc
Source_line: 4627
…
```




show profile能够在做SQL优化时帮助我们了解时间都耗费到哪里去了。而MySQL 5.6则通过trace文件进一步向我们展示了优化器是如何选择执行计划的。

