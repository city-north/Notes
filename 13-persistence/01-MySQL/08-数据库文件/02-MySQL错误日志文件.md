# 错误日志文件

## 错误日志存放地址

一般存放在数据目录下,以 error.log 作为文件名的结尾

```sql
show variables like '%log_error'
```

查询结果

```json
[
  {
    "Variable_name": "log_error",
    "Value": "/usr/local/mysql/data/mysqld.local.err"
  }
]
```

类似于 Oracle 的 alert log, 错误日志记录MySQL 的启动、运行、关闭过程中出现的问题, 尤其作为初学者,要学会利用 error log 来定位问题

错误日志不光记录着错误的信息, 在 MySQL 5.7 初始化数据库中, 加上 --initialize 参数, 会生成一个临时的数据库初始化密码,记录在 log-error 中