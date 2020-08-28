# 备份恢复

MySQL 数据库按照其服务的运行状态(停库和非停库) 我们可以分为冷备和热备

- [冷备以及恢复](#冷备以及恢复)
- [热备以及恢复](#热备以及恢复)

## 冷备以及恢复

冷备的意思就是数据库出于关闭状态下的备份

- 优势
  - 保证数据库备份的完整性
  - 备份过程简单并且恢复速度快

- 缺点
  - 停库

**停止 MySQL 服务**

```
/usr/local/mysql/bin/mysqladmin -uroot -proot shutdown
```

复制整个目录

```
Scp -r /data/mysql/  root@远程备份机ip:/新目录
Copy -r /data/mysql/  -- 本地新目录
```

恢复的话就替换

## 热备以及恢复

热备份就是数据库出于运行状态下的备份,不影响现有业务正常进行

分为 

- 逻辑备份
  -  [010-mysqldump的备份与恢复.md](010-mysqldump的备份与恢复.md) 
  -  [020-select-into-outfile备份与恢复.md](020-select-into-outfile备份与恢复.md) 
  -  [030-mydumper备份与恢复.md](030-mydumper备份与恢复.md) 
  -  [040-裸文件备份XtraBackup.md](040-裸文件备份XtraBackup.md) 
  -  [050-binlog2sql闪回.md](050-binlog2sql闪回.md) 
- 裸文件备份



