[返回目录](/README.md)

# 安装Oracle11g

演示环境：Oracle Linux 7.3

Oracle版本：Oracle .x64\_11gR2

## 系统要求

* 内存：至少1G，推荐2G以上

检查命令：

```
grep MemTotal /proc/meminfo
```

* 硬盘：
  * 企业版安装需要4.29G和1.7G的数据文件,
  * tmp目录下至少1g空闲内存
* [交换内存（_swap space_）要求](./software/swapSpace.md)

## 前期准备

* 安装包两个，上传到/opt文件夹下

  * linux.x64\_11gR2\_database\_1of2.zip

  * linux.x64\_11gR2\_database\_2of2.zip

* 官方快速安装文档

  * [Oracle Database 11g Quick Installation Guide.pdf](../assets/Oracle Database 11g Quick Installation Guide.pdf)

* 安装基础package\(32位可忽略\)

```
yum install binutils
yum install compat-libcap1
yum install compat-libstdc++-33-3.2.3-71.el7.x86_64
yum install gcc
yum install gcc-c++
yum install glibc
yum install glibc-devel
yum install ksh
yum install libaio
yum install libaio-devel
yum install libgcc-4.8.2
yum install libstdc++
yum install libstdc++-devel
yum install libXi
yum install libXtst
yum install make
yum install sysstat
yum install unzip
yum install elfutils-libelf-devel
yum install unixODBC
yum install unixODBC-devel
```

## 开始安装

### 修改主机名

```
sed -i "s/HOSTNAME=localhost.localdomain/HOSTNAME=oracledb/" /etc/sysconfig/network
hostname oracledb
```

### 添加主机名与IP对应记录

```
vim /etc/hosts 
10.10.0.48    oracledb
```

### 关闭Selinux

```
sed -i "s/SELINUX=enforcing/SELINUX=disabled/" /etc/selinux/config  
setenforce 0
```

### 创建用户和组

```
//创建Oracle安装组oinstall
groupadd -g 200 oinstall 
//创建数据库管理员组dba
groupadd -g 201 dba 
//创建oracle用户
useradd -u 440 -g oinstall -G dba oracle 
passwd oracle
//输入密码
```

### 修改内核参数

```
 vim /etc/sysctl.conf
```

添加以下参数

```
net.ipv4.ip_local_port_range= 9000 65500 
fs.file-max = 6815744 
kernel.shmall = 10523004 
kernel.shmmax = 6465333657 
kernel.shmmni = 4096 
kernel.sem = 250 32000 100 128 
net.core.rmem_default=262144 
net.core.wmem_default=262144 
net.core.rmem_max=4194304 
net.core.wmem_max=1048576 
fs.aio-max-nr = 1048576
```

使配置生效

```
sysctl -p
```

### 修改系统资源限制

```
 vim /etc/security/limits.conf
```

末尾添加

```
oracle  soft  nproc  2047 
oracle  hard  nproc  16384 
oracle  soft  nofile  1024 
oracle  hard  nofile  65536
```

### 修改用户验证选项

```
vim /etc/pam.d/login
```

在`session    required    pam_namespace.so`  下面添加一条：

session    required    pam\_limits.so

### 修改用户配置文件

```
vim /etc/profile
```

添加：

```
if [ $USER ="oracle" ]; then
      if [ $SHELL = "/bin/ksh" ];then
          ulimit -p 16384 
          ulimit -n 65536 
      else
          ulimit -u 16384 -n 65536 
      fi
fi
```

### 创建安装目录及设置权限

```
mkdir -p /opt/app/oracle/
chmod 755 /opt/app/oracle/ 
chown oracle.oinstall -R /opt/app/oracle/
```

### 设置oracle环境变量

```
su - oracle
vim  ~/.bash_profile
```

#### 设置oracle

```
export ORACLE_BASE=/opt/app/oracle
export ORACLE_HOME=$ORACLE_BASE/product/11.2.0/db_1
export PATH=$PATH:$HOME/bin:$ORACLE_HOME/bin
#export PATH=$PATH:$ORACLE_HOME/bin
export ROACLE_PID=ora11g
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8
export LD_LIBRARY_PATH=$ORACLE_HOME/lib:/usr/lib
export ORACLE_SID=orcl
```

#### 生效文件

```
source ~/.bash_profile
```

#### 查看环境变量是否完成

```
env | grep ORA
```

以下为正常输出

```
[oracle@oracledb ~]$ env | grep ORA
ORACLE_BASE=/opt/app/oracle
ORACLE_HOME=/opt/app/oracle/product/11.2.0/db_1
```

使用一下命令完成

```
echo $ORACLE_HOME
echo $ORACLE_SID
```

手动插入

```
export ORACLE_SID=orcl
```

## 安装Oracle

### 解压\(root用户\)

```
cd /opt
unzip linux.x64_11gR2_database_1of2.zip
unzip linux.x64_11gR2_database_2of2.zip
```

解压后得到database目录

### 解压后文件介绍

database/response目录：

* db\_install.rsp：安装应答

* dbca.rsp：创建数据库应答

* netca.rsp：建立监听、本地服务名等网络设置的应答

### 赋予oracle使用权限

```
chmod 755 /opt/database/ 
chown oracle.oinstall -R /opt/database/
```

### 修改db\_install.rsp文件

一共48项

```
oracle.install.responseFileVersion=/oracle/install/rspfmt_dbinstall_response_schema_v11_2_0
oracle.install.option=INSTALL_DB_SWONLY
ORACLE_HOSTNAME=oracledb
UNIX_GROUP_NAME=oinstall
INVENTORY_LOCATION=/opt/app/oracle/oraInventory
SELECTED_LANGUAGES=en,zh_CN
ORACLE_HOME=/opt/app/oracle/product/11.2.0/db_1
ORACLE_BASE=/opt/app/oracle
oracle.install.db.InstallEdition=EE
oracle.install.db.isCustomInstall=false
oracle.install.db.customComponents=oracle.server:11.2.0.1.0,oracle.sysman.ccr:10.2.7.0.0,oracle.xdk:11.2.0.1.0,oracle.rdbms.oci:11.2.0.1.0,oracle.network:11.2.0.1.0,oracle.network.listener:11.2.0.1.0,oracle.rdbms:11.2.0.1.0,oracle.options:11.2.0.1.0,oracle.rdbms.partitioning:11.2.0.1.0,oracle.oraolap:11.2.0.1.0,oracle.rdbms.dm:11.2.0.1.0,oracle.rdbms.dv:11.2.0.1.0,orcle.rdbms.lbac:11.2.0.1.0,oracle.rdbms.rat:11.2.0.1.0
oracle.install.db.DBA_GROUP=dba
oracle.install.db.OPER_GROUP=oinstall
oracle.install.db.CLUSTER_NODES=
oracle.install.db.config.starterdb.type=GENERAL_PURPOSE
oracle.install.db.config.starterdb.globalDBName=ora11g
oracle.install.db.config.starterdb.SID=ora11g
oracle.install.db.config.starterdb.characterSet=AL32UTF8
oracle.install.db.config.starterdb.memoryOption=true
oracle.install.db.config.starterdb.memoryLimit=1500
oracle.install.db.config.starterdb.installExampleSchemas=false
oracle.install.db.config.starterdb.enableSecuritySettings=true
oracle.install.db.config.starterdb.password.ALL=oracle
oracle.install.db.config.starterdb.password.SYS=
oracle.install.db.config.starterdb.password.SYSTEM=
oracle.install.db.config.starterdb.password.SYSMAN=
oracle.install.db.config.starterdb.password.DBSNMP=
oracle.install.db.config.starterdb.control=DB_CONTROL
oracle.install.db.config.starterdb.gridcontrol.gridControlServiceURL=
oracle.install.db.config.starterdb.dbcontrol.enableEmailNotification=false
oracle.install.db.config.starterdb.dbcontrol.emailAddress=
oracle.install.db.config.starterdb.dbcontrol.SMTPServer=
oracle.install.db.config.starterdb.automatedBackup.enable=false
oracle.install.db.config.starterdb.automatedBackup.osuid=
oracle.install.db.config.starterdb.automatedBackup.ospwd=
oracle.install.db.config.starterdb.storageType=FILE_SYSTEM_STORAGE
oracle.install.db.config.starterdb.fileSystemStorage.dataLocation=
oracle.install.db.config.starterdb.fileSystemStorage.recoveryLocation=
oracle.install.db.config.asm.diskGroup=
oracle.install.db.config.asm.ASMSNMPPassword=
MYORACLESUPPORT_USERNAME=
MYORACLESUPPORT_PASSWORD=
SECURITY_UPDATES_VIA_MYORACLESUPPORT=
DECLINE_SECURITY_UPDATES=true
PROXY_HOST=
PROXY_PORT=
PROXY_USER=
PROXY_PWD=
```

### 静默安装

```
[oracle@oracledb ~]$ cd /opt/database/
[oracle@oracledb database]$ ./runInstaller -silent -force -responseFile /opt/database/response/db_install.rsp
```

报错：

```
准备从以下地址启动 Oracle Universal Installer /tmp/OraInstall2018-07-08_02-06-29PM. 
请稍候...[oracle@oracledb database]$ [WARNING] [INS-32055] 主产品清单位于 Oracle 基目录中。
   原因: 主产品清单位于 Oracle 基目录中。
   操作: Oracle 建议将此主产品清单放置在 Oracle 基目录之外的位置中。
[FATAL] [INS-13013] 目标环境不满足一些必需要求。
   原因: 不满足一些必需的先决条件。有关详细信息, 请查看日志。
   /tmp/OraInstall2018-07-08_02-06-29PM/installActions2018-07-08_02-06-29PM.log
   操作: 从日志 /tmp/OraInstall2018-07-08_02-06-29PM/installActions2018-07-08_02-06-29PM.log 
   中确定失败的先决条件检查列表。然后, 从日志文件或安装手册中查找满足这些先决条件的适当配置, 并手动进行修复。
此会话的日志当前已保存为: /tmp/OraInstall2018-07-08_02-06-29PM/installActions2018-07-08_02-06-29PM.log。
如果要保留此日志, Oracle 建议将它从临时位置移动到更持久的位置。
```

打开`/tmp/OraInstall2018-07-08_02-06-29PM/installActions2018-07-08_02-06-29PM.log`查看错误

如果交换空间大小: 此先决条件将测试系统是否具有足够的总交换空间。参考[交换空间](/software/swapSpace.md)

如果操作系统内核参数: semmni,可忽略

如果是pdksh-5包缺失，忽略

## 除了以上三条，其他均满足，使用下面命令跳过检查：

```
[oracle@oracledb ~]$ cd /opt/database/
[oracle@oracledb database]$ ./runInstaller -ignorePrereq -silent -force -responseFile /opt/database/response/db_install.rsp
```

出现下图，表示完成安装

![](/assets/import03.png)

### 执行脚本（root权限）

```
/opt/app/oracle/oraInventory/orainstRoot.sh
/opt/app/oracle/product/11.2.0/db_1/root.sh
```

## 配置监听程序

```
$ORACLE_HOME/bin/netca /silent /responseFile /opt/database/response/netca.rsp
```

### 启动监听程序

```
 /opt/app/oracle/product/11.2.0/db_1/bin/lsnrctl start LISTENER
```

## 创建数据库实例

```
vim /opt/database/response/dbca.rsp
```

参考：根据自己需求修改

```
GDBNAME = "ora11g.dg01"78 行 全局数据库的名字=SID+主机域名
SID="orcl" //149行 SID
SYSPASSWORD ="oracle"//SYS管理员密码
SYSTEMPASSWORD ="oracle"//SYSTEM管理员密码
SYSMANPASSWORD= "oracle"
DBSNMPPASSWORD= "oracle"
TOTALMEMORY ="1638"//1638MB，物理内存2G*80%。
CHARACTERSET="AL32UTF8" //415行 编码
NATIONALCHARACTERSET="UTF8" //425行 编码
```

开始安装

```
$ORACLE_HOME/bin/dbca -silent -responseFile /opt/database/response/dbca.rsp
```

实例进程检查

```
ps -ef | grep ora_ | grep -v grep
```

```
[oracle@localhost database]$ ps -ef | grep ora_ | grep -v grep
oracle     6368      1  0 15:21 ?        00:00:00 ora_pmon_orcl
oracle     6370      1  0 15:21 ?        00:00:00 ora_vktm_orcl
oracle     6374      1  0 15:21 ?        00:00:00 ora_gen0_orcl
oracle     6376      1  0 15:21 ?        00:00:00 ora_diag_orcl
oracle     6378      1  0 15:21 ?        00:00:00 ora_dbrm_orcl
oracle     6380      1  0 15:21 ?        00:00:00 ora_psp0_orcl
oracle     6382      1  0 15:21 ?        00:00:00 ora_dia0_orcl
oracle     6384      1  0 15:21 ?        00:00:00 ora_mman_orcl
oracle     6386      1  0 15:21 ?        00:00:00 ora_dbw0_orcl
oracle     6388      1  0 15:21 ?        00:00:00 ora_lgwr_orcl
oracle     6390      1  0 15:21 ?        00:00:00 ora_ckpt_orcl
oracle     6392      1  0 15:21 ?        00:00:00 ora_smon_orcl
oracle     6394      1  0 15:21 ?        00:00:00 ora_reco_orcl
oracle     6396      1  0 15:21 ?        00:00:00 ora_mmon_orcl
oracle     6398      1  0 15:21 ?        00:00:00 ora_mmnl_orcl
oracle     6400      1  0 15:21 ?        00:00:00 ora_d000_orcl
oracle     6402      1  0 15:21 ?        00:00:00 ora_s000_orcl
oracle     6476      1  0 15:21 ?        00:00:00 ora_qmnc_orcl
oracle     6491      1  0 15:21 ?        00:00:00 ora_cjq0_orcl
oracle     6493      1  0 15:21 ?        00:00:00 ora_q000_orcl
oracle     6495      1  0 15:21 ?        00:00:00 ora_q001_orcl
```

监听状态检查

```
lsnrctl status
```

```
[oracle@localhost database]$ lsnrctl status

LSNRCTL for Linux: Version 11.2.0.1.0 - Production on 08-JUL-2018 15:26:37

Copyright (c) 1991, 2009, Oracle.  All rights reserved.

Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=IPC)(KEY=EXTPROC1521)))
STATUS of the LISTENER
------------------------
Alias                     LISTENER
Version                   TNSLSNR for Linux: Version 11.2.0.1.0 - Production
Start Date                08-JUL-2018 15:04:50
Uptime                    0 days 0 hr. 21 min. 47 sec
Trace Level               off
Security                  ON: Local OS Authentication
SNMP                      OFF
Listener Parameter File   /opt/app/oracle/product/11.2.0/db_1/network/admin/listener.ora
Listener Log File         /opt/app/oracle/diag/tnslsnr/localhost/listener/alert/log.xml
Listening Endpoints Summary...
  (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))
  (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1521)))
Services Summary...
Service "ora11g.dg01" has 1 instance(s).
  Instance "orcl", status READY, has 1 handler(s) for this service...
Service "orclXDB.dg01" has 1 instance(s).
  Instance "orcl", status READY, has 1 handler(s) for this service...
The command completed successfully
```

修改dbstart文件

```
$ vim /opt/app/oracle/product/11.2.0/db_1/bin/dbstart
```

第80行：

```
ORACLE_HOME_LISTNER=$1修改为ORACLE_HOME_LISTNER=$ORACLE_HOME
```

修改dbshut文件

```
$ vim /opt/app/oracle/product/11.2.0/db_1/bin/dbshut
```

第50行：

```
ORACLE_HOME_LISTNER=$1修改为ORACLE_HOME_LISTNER=$ORACLE_HOME
```

修改/etc/oratab

```
$vim /etc/oratab
```

```
orcl:/opt/app/oracle/product/11.2.0/db_1:N改为orcl:/opt/app/oracle/product/11.2.0/db_1:Y
```

### 测试

查看实例

```
sqlplus / as sysdba
SQL>select status from v$instance;
```

测试关闭命令

```
$ dbshut
```

Oracle监听停止，进程消失

```
$lsnrctl status
$ps -ef |grep ora_ |grep -v grep
```

测试开启命令

```
$ dbstart
```

Oracle监听启动，进程启动。

```
$ lsnrctl status
$ ps -ef |grep ora_ |grep -v grep
```

## 打开seLinux

```
vim /etc/selinux/config
```

```
SELINUXTYPE=targeted
```

## 修改监听

```
vim /opt/app/oracle/product/11.2.0/db_1/network/admin/listener.ora
```

```
# listener.ora Network Configuration File: /opt/app/oracle/product/11.2.0/db_1/network/admin/listener.ora
# Generated by Oracle configuration tools.


SID_LIST_LISTENER =
  (SID_LIST =
   (SID_DESC =
      (SID_NAME = orcl)
      (ORACLE_HOME = /opt/app/oracle/product/11.2.0/db_1)
      (GLOBAL_DBNAME= orcl)
    )
  )


LISTENER =
  (DESCRIPTION_LIST =
    (DESCRIPTION =
      (ADDRESS = (PROTOCOL = IPC)(KEY = EXTPROC1521))
      (ADDRESS = (PROTOCOL = TCP)(HOST = oracledb)(PORT = 1521))
    )
  )

ADR_BASE_LISTENER = /opt/app/oracle
```

```

```

### 命令：

开启oracle服务：

```
$dbstart
$lsnrctl start
$sqlplus / as sysdba
SQL>startup
```

关闭oracle服务

```
$dbshut
$lsnrctl stop
$sqlplus / as sysdba
SQL>shutdown
```

[返回目录](/README.md)

