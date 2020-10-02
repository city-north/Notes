# ACT_GE*相关表结构

`‘RE’表示repository`
这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。

![](https://www.showdoc.cc/server/api/common/visitfile/sign/782835601f99385ffd7b3b7e4a9af8a4?showdoc=.jpg)


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

```sql
CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Key',
  `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT 'Value',
  `REV_` int(11) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`NAME_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='资源表(存储流程定义相关的数据)';
```