# 属性表-act_ge_property

[TOC]

这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。

```
act_ge_bytearray 属性表(保存流程引擎的kv键值属性)
act_ge_property  资源表(存储流程定义相关的数据)
```

## 表结构

```sql
CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Key',
  `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT 'Value',
  `REV_` int(11) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`NAME_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='资源表(存储流程定义相关的数据)';
```

## 列信息

| COLUMN\_NAME | COLUMN\_TYPE   | column\_comment |
| :----------- | :------------- | :-------------- |
| name\_       | varchar\(64\)  | 属性名称        |
| value\_      | varchar\(300\) | 属性值          |
| rev\_        | int\(11\)      | 数据的版本号    |

## 数据实例

| name\_属性名称                       | value\_属性值                                    | rev\_ |
| :----------------------------------- | :----------------------------------------------- | :---- |
| cfg.execution-related-entities-count | false                                            | 1     |
| next.dbid                            | 235001                                           | 95    |
| schema.history                       | create\(6.0.0.3\) upgrade\(6.0.0.3-&gt;6.0.0.4\) | 2     |
| schema.version                       | 6.0.0.4                                          | 2     |

