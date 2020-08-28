# 全量日志(general log)

general log 会记录 MySQL 数据库所有操作的 SQL 语句, 包括 select 和 show , 默认关闭

## 个别情况下可以开启

一般用于故障检测

```
show variables like '%log_output%'
```

查询结果

| Variable\_name | Value |
| :------------- | :---- |
| log\_output    | FILE  |

- FILE

  > 这种存储方式可以很方便的检索

- TABLE

  > 这种存储方式,会在 MySQL数据库中创建一个 general_log 表

- NONE 

  > 即使 general_log 开启了也不会记录日志