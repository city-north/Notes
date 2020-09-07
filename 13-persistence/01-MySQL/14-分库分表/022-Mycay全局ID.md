# Mycat全局ID

Mycat 全局序列实现方式主要有 4 种:

- [文件方式](#文件方式)
- [数据库方式](#数据库方式)
- [本地时间戳方式](#本地时间戳方式)
- [zookeeper方式](#zookeeper方式)

当然也可以自定义业务序列。

## 文件方式

配置文件 server.xml sequnceHandlerType 值: 0 文件 1数据库 2 本地时间戳 3ZK

```
<property name="sequnceHandlerType">0</property>
```

文件方式，配置 conf/sequence_conf.properties

```
CUSTOMER.HISIDS= 
CUSTOMER.MINID=10000001 
CUSTOMER.MAXID=20000000 
CUSTOMER.CURID=10000001
```

语法:

```
select next value for MYCATSEQ_CUSTOMER
```

实例

```
INSERT INTO `customer` (`id`, `name`) VALUES (next value for MYCATSEQ_CUSTOMER, 'qingshan');
```

- 优点:本地加载，读取速度较快。
- 缺点:当 Mycat 重新发布后，配置文件中的 sequence 需要替换。Mycat 不能 做集群部署。

## 数据库方式

```xml
<property name="sequnceHandlerType">1</property>

```

配置: sequence_db_conf.properties
把这张表创建在 146 上，所以是 dn1

```
#sequence stored in datanode
GLOBAL=dn1 
CUSTOMER=dn1
```

在第一个数据库节点上创建 MYCAT_SEQUENCE 表:

```sql
DROP TABLE IF EXISTS MYCAT_SEQUENCE; CREATE TABLE MYCAT_SEQUENCE (
name VARCHAR(50) NOT NULL,
current_value INT NOT NULL,
increment INT NOT NULL DEFAULT 1, remark varchar(100),
PRIMARY KEY(name)) ENGINE=InnoDB;
```

注:可以在 schema.xml 配置文件中配置这张表，供外部访问。

```
<table name="mycat_sequence" dataNode="dn1" autoIncrement="true" primaryKey="id"></table>
```

创建存储过程——获取当前 sequence 的值

```
DROP FUNCTION IF EXISTS `mycat_seq_currval`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `mycat_seq_currval`(seq_name VARCHAR(50)) RETURNS varchar(64) CHARSET latin1
DETERMINISTIC
BEGIN
DECLARE retval VARCHAR(64);
SET retval="-999999999,null";
SELECT concat(CAST(current_value AS CHAR),",",CAST(increment AS CHAR) ) INTO retval FROM MYCAT_SEQUENCE WHERE name = seq_name;
RETURN retval ;
END
;;
DELIMITER ;
```

创建存储过程，获取下一个 sequence

```
DROP FUNCTION IF EXISTS `mycat_seq_nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `mycat_seq_nextval`(seq_name VARCHAR(50)) RETURNS varchar(64) CHARSET latin1
DETERMINISTIC
BEGIN
UPDATE MYCAT_SEQUENCE
SET current_value = current_value + increment WHERE name = seq_name; RETURN mycat_seq_currval(seq_name);
END
;;
DELIMITER ;
```

创建存储过程，设置 sequence

```
DROP FUNCTION IF EXISTS `mycat_seq_setval`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `mycat_seq_setval`(seq_name VARCHAR(50), value INTEGER) RETURNS varchar(64) CHARSET latin1
DETERMINISTIC
BEGIN
UPDATE MYCAT_SEQUENCE
SET current_value = value
WHERE name = seq_name;
RETURN mycat_seq_currval(seq_name);
END
;;
DELIMITER ;
```

插入记录

```
INSERT INTO MYCAT_SEQUENCE(name,current_value,increment,remark) VALUES ('GLOBAL', 1, 100,''); INSERT INTO MYCAT_SEQUENCE(name,current_value,increment,remark) VALUES ('ORDERS', 1, 100,'订单表使 用');
```

测试

```
select next value for MYCATSEQ_ORDERS
```

## 本地时间戳方式

ID= 64 位二进制 (42(毫秒)+5(机器 ID)+5(业务编码)+12(重复累加) ，长度为 18 位

```
<property name="sequnceHandlerType">2</property>
```

配置文件 sequence_time_conf.properties

```
#sequence depend on TIME
WORKID=01 DATAACENTERID=01
```

## zookeeper方式

修改 conf/myid.properties
设置 loadZk=true(启动时会从 ZK 加载配置，一定要注意备份配置文件，并且先 用 bin/init_zk_data.sh,把配置文件写入到 ZK)

```
<property name="sequnceHandlerType">3</property>
```

配置文件:sequence_distributed_conf.properties

```
# 代表使用 zk
INSTANCEID=ZK
# 与 myid.properties 中的 CLUSTERID 设置的值相同 CLUSTERID=010
```

复制配置文件

```
cd /usr/local/soft/mycat/conf
cp *.txt *.xml *.properties zkconf/ chown -R zkconf/
cd /usr/local/soft/mycat/bin ./init_zk_data.sh
```

验证:select next value for MYCATSEQ_GLOBAL

使用
在 schema.xml 的 table 标签上配置 autoIncrement="true"，不需要获取和指定 序列的情况下，就可以使用全局 ID 了。