[返回目录](/README.md)

# 事务

数据库事务是由一个或多个相关SQL语句组成的原子工作单元。它被称为原子操作，因为构成事务的SQL语句带来的数据库修改可以共同提交，即永久化到数据库或从数据库回滚\(撤销\)。

成功执行的SQL语句和提交的事务不一样。即使成功执行SQL语句，除非提交包含语句的事务，否则可以回滚该语句，并且可以撤消语句所做的所有更改。

## 开始和结束事务

事务有开始和结束。当发生以下事件之一时，事务即开始 -

* 连接到数据库后执行第一个SQL语句。
* 在事务完成后发出的每个新的SQL语句。

事务在下列事件之一发生时结束 -

* 发出了`COMMIT`或`ROLLBACK`语句。
* 发出_DDL_语句，例如：`CREATE TABLE`语句; 因为在这种情况下，自动执行`COMMIT`。
* 发布`DCL`语句，如:`GRANT`声明; 因为在这种情况下，自动执行`COMMIT`。
* 用户从数据库断开连接。用户通过发出`EXIT`
* 命令从SQL \* PLUS退出，`COMMIT`自动执行。
* SQL \* Plus异常终止，会自动执行`ROLLBACK`。
* DML语句失败; 在这种情况下，会自动执行`ROLLBACK`来撤消该`DML`语句。

## 提交事务

通过发出SQL命令`COMMIT`将事务永久化。`COMMIT`

命令的一般语法是 

```
COMMIT
```

例如

```
INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (1, 'Ramesh', 32, 'Ahmedabad', 2000.00 ); 

INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (2, 'Khilan', 25, 'Delhi', 1500.00 ); 

INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (3, 'kaushik', 23, 'Kota', 2000.00 ); 

INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (4, 'Chaitali', 25, 'Mumbai', 6500.00 ); 

INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (5, 'Hardik', 27, 'Bhopal', 8500.00 ); 

INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (6, 'Komal', 22, 'MP', 4500.00 ); 

COMMIT;
```

## 回滚事务

使用`ROLLBACK`命令可以撤销对不带`COMMIT`的数据库所做的更改。

`ROLLBACK`命令的一般语法是 -

```
ROLLBACK [TO SAVEPOINT < savepoint_name>];
```

当事务由于某种前所未有的情况而中止，如系统故障时，自提交以来整个事务被自动回滚。 如果不使用保存点\(`savepoint`\)，那么只需使用以下语句来回滚所有更改。

```
ROLLBACK;
```

#### 保存点 - Savepoints {#h4--savepoints}

保存点\(`Savepoints`\)是有助于通过设置一些检查点将长事务拆分成更小的单元的标记。通过在长事务中设置保存点，如果需要，可以回滚到检查点。这是通过发出`SAVEPOINT`命令完成的。

`SAVEPOINT`命令的一般语法是 -

```
SAVEPOINT < savepoint_name >;
```

```
INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (7, 'Rajnish', 27, 'HP', 9500.00 ); 

INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY) 
VALUES (8, 'Riddhi', 21, 'WB', 4500.00 ); 
SAVEPOINT sav1;

UPDATE CUSTOMERS 
SET SALARY = SALARY + 1000; 
ROLLBACK TO sav1;

UPDATE CUSTOMERS 
SET SALARY = SALARY + 1000 
WHERE ID = 7; 
UPDATE CUSTOMERS 
SET SALARY = SALARY + 1000 
WHERE ID = 8; 

COMMIT;
```

`ROLLBACK TO sav1`- 此语句回滚直到保存点`sav1`的所有更改。

之后，所做新的改变将重新开始。



## 自动事务控制 {#h2-u81EAu52A8u4E8Bu52A1u63A7u5236}

要在执行`INSERT`，`UPDATE`或`DELETE`命令时自动执行`COMMIT`，可以将`AUTOCOMMIT`环境变量设置为 -

```
SET AUTOCOMMIT ON;
```

也可以使用以下命令关闭事务自动提交模式 -

```
SET AUTOCOMMIT OFF;
```



