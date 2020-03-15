# MySQL

MySQL 的发展历史和版本分支:

- 1996 年
  MySQL 1.0 发布。它的历史可以追溯到 1979 年，作者 Monty 用 BASIC 设计 的一个报表工具。

- 1996年10月
  3.11.1 发布。MySQL 没有 2.x 版本。
- 2000 年
  ISAM 升级成 MyISAM 引擎。MySQL 开源。
- 2003 年
  MySQL 4.0 发布，集成 InnoDB 存储引擎。
- 2005 年
  MySQL 5.0 版本发布，提供了视图、存储过程等功能。
- 2008 年
  MySQL AB 公司被 Sun 公司收购，进入 Sun MySQL 时代。
- 2009 年
  Oracle 收购 Sun 公司，进入 Oracle MySQL 时代。
- 2010 年
  MySQL 5.5 发布，InnoDB 成为默认的存储引擎。
- 2016 年
  MySQL 发布 8.0.0 版本。为什么没有 6、7?5.6 可以当成 6.x，5.7 可以当 成 7.x。

## MySQL 分支

MariaDB，CentOS 7 里面自带了一个 MariaDB。它是怎么来的呢? Oracle 收购 MySQL 之后，MySQL 创始人之一 Monty 担心 MySQL 数据库 发展的未来(开发缓慢，封闭，可能会被闭源)，就创建了一个分支 MariaDB，默认使 用全新的 Maria 存储引擎，它是原 MyISAM 存储引擎的升级版本。

Percona Server 是 MySQL 重要的分支之一，它基于 InnoDB 存储引擎的基础上， 提升了性能和易管理性，最后形成了增强版的 XtraDB 引擎，可以用来更好地发挥服务器 硬件上的性能。

国内也有一些 MySQL 的分支或者自研的存储引擎，比如网易的 InnoSQL，极数云 舟的 ArkDB。

