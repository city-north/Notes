# ACT_GE*相关表结构-通用数据库

## 目录

- [资源表-act_ge_bytearray](#资源表-act_ge_bytearray)
- [属性表-act_ge_property](#属性表-act_ge_property)

`RE’表示repository

这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。

```
act_ge_bytearray 属性表(保存流程引擎的kv键值属性)
act_ge_property  资源表(存储流程定义相关的数据)
```

## 资源表-act_ge_bytearray


```sql
CREATE TABLE `act_ge_bytearray` (
	`ID_` VARCHAR ( 64 ) COLLATE utf8_bin NOT NULL,
	`REV_` INT ( 11 ) DEFAULT NULL COMMENT '版本信息',
	`NAME_` VARCHAR ( 255 ) COLLATE utf8_bin DEFAULT NULL,
	`DEPLOYMENT_ID_` VARCHAR ( 64 ) COLLATE utf8_bin DEFAULT NULL COMMENT '部署 ID',
	`BYTES_` LONGBLOB COMMENT '二进制流内容,如流程定义 xml',
	`GENERATED_` TINYINT ( 4 ) DEFAULT NULL,
	PRIMARY KEY ( `ID_` ),
	KEY `ACT_FK_BYTEARR_DEPL` ( `DEPLOYMENT_ID_` ),
CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY ( `DEPLOYMENT_ID_` ) REFERENCES `act_re_deployment` ( `ID_` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8 COLLATE = utf8_bin COMMENT = '属性表(保存流程引擎的 KV 键值属性)';
```

#### 列信息

| COLUMN\_NAME     | COLUMN\_TYPE   | column\_comment                                              |
| :--------------- | :------------- | :----------------------------------------------------------- |
| id\_             | varchar\(64\)  |                                                              |
| rev\_            | int\(11\)      | 数据版本,Activit为一些有可能被频繁修改的数据表,加入该字段,用来标识该数据被操作的次数 |
| name\_           | varchar\(255\) | 资源名称,类型是varchar , 长度时255字节                       |
| deployment\_id\_ | varchar\(64\)  | 一次部署可以添加多个资源,该字段与部署表 ACT_RE_DEPLOYMENT的主键相关联 |
| bytes\_          | longblob       | 资源内容,数据类型为long                                      |
| generated\_      | tinyint\(4\)   | 是否由Activiti自动产生的资源, 0 代表false, 1 代表true        |

## 属性表-act_ge_property

```sql
CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Key',
  `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT 'Value',
  `REV_` int(11) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`NAME_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='资源表(存储流程定义相关的数据)';
```

#### 列信息

| COLUMN\_NAME | COLUMN\_TYPE   | column\_comment |
| :----------- | :------------- | :-------------- |
| name\_       | varchar\(64\)  | 属性名称        |
| value\_      | varchar\(300\) | 属性值          |
| rev\_        | int\(11\)      | 数据的版本号    |

