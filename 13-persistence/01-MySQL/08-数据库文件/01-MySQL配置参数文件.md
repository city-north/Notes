# 01-MySQL配置参数文件

[TOC]

## 配置文件读取顺序

启动 MySQL 过程中

- /etc/my.cnf
- /etc/mysql/my.cnf
- /usr/local/mysql/my.cnf
- ~/my.cnf

在 my.cnf 文件中, 分为 client section 和 server section 两块

- client section 

  > 用来配置 MySQL 客户端的参数

- server section 

  > 用来配置 MySQL服务端的核心参数

