[返回目录](/README.md)

# SQL简介

* DML\(数据库操作语言\)：INSERT/UPDATE/DELETE,动作查询语言
* DDL\(数据库定义语言\)：CREATE/DROP/添加索引等
* DCL\(数据库控制语言\)：通过GRANT或者REVOKE获得许可，确定单个用户和用户组队数据库对象的访问，某些RDBMS可用GRANT或REVOKE控制对表单个列的访问。

## 使用sqlplus 连接Oracle

```
C:\Users\EricChen>sqlplus scott/tiger@192.168.25.128/orcl

SQL*Plus: Release 12.1.0.1.0 Production on Tue Jun 12 20:07:40 2018

Copyright (c) 1982, 2013, Oracle.  All rights reserved.


Connected to:
Oracle Database 10g Enterprise Edition Release 10.2.0.1.0 - Production
With the Partitioning, OLAP and Data Mining options
```

## 开始录制

```
spool on
```

## 当前用户

```
USER 为 "SCOTT"
```

## 查询当前用户下的表

```
SQL>  --当前用户下的表
SQL> select * from tab;

TNAME                          TABTYPE  CLUSTERID                               
------------------------------ ------- ----------                               
DEPT                           TABLE                                            
EMP                            TABLE                                            
BONUS                          TABLE                                            
SALGRADE                       TABLE                                            
```

## 查看表结构

```
SQL> --员工表的结构
SQL> desc emp
 名称                                      是否为空? 类型
 ----------------------------------------- -------- ----------------------------
 EMPNO                                     NOT NULL NUMBER(4)
 ENAME                                              VARCHAR2(10)
 JOB                                                VARCHAR2(9)
 MGR                                                NUMBER(4)
 HIREDATE                                           DATE
 SAL                                                NUMBER(7,2)
 COMM                                               NUMBER(7,2)
 DEPTNO                                             NUMBER(2)
```

## 清屏

```
SQL> --清屏
SQL> host cls
```

## 设置行宽

```
SQL> show linesize
linesize 80
SQL> set linesize 120
```

## 设置列宽

```
SQL> --设置列宽
SQL> col ename for a8
SQL> col sal for 9999
SQL> /
```

## SQL中的Null

* 包含null的表达式都为null
* null永远!=null

## dual表：伪表

```
SQL> select 'Hello'||'  World' 字符串 from dual;
字符串                                                                                                                  
------------                                                                                                            
Hello  World    

                                                                                            
SQL> --查询员工信息：***的薪水是****
SQL> select ename||'的薪水是'||sal 信息 from emp;

信息                                                                                                                    
----------------------------------------------------------                                                              
SMITH的薪水是800                                                                                                        
ALLEN的薪水是1600                                                                                                       
WARD的薪水是1250                                                                                                        
JONES的薪水是2975                                                                                                       
MARTIN的薪水是1250                                                                                                      
BLAKE的薪水是2850                                                                                                       
CLARK的薪水是2450                                                                                                       
SCOTT的薪水是3000                                                                                                       
KING的薪水是5000                                                                                                        
TURNER的薪水是1500                                                                                                      
ADAMS的薪水是1100                                                                                                                                                                                          
```

* ## 结束录制

```
spool off
```



