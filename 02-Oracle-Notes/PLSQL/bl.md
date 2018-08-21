[返回目录](/README.md)

# PL/SQL变量与常量

在PL/SQL程序运行时，需要定义一些变量来存放一些数据，PL/SQL中的常量和变量定义介绍如下。

## 定义常量

定义常量的语句格式如下：

```
<常量名> constant <数据类型> :=<值>;
```

constant 表示在定义常量，常量一旦定义，在以后的使用中其值将不再改变。一些固定大小，为了防止有人改变，可以使用。

```
MonthCount constant INTEGER:=12
```

## 定义变量

定义变量的语句格式如下：

```
<变量名><数据类型>[(宽度):=<初始化>];
```

```
address VARCHAR@(30);
```

PL/SQL定义了一个未初始化变量应该存放的内容，被赋值为NULL。

初始化:

```
counter binary_integer := 0; 
greetings varchar2(20) DEFAULT 'Have a Good Day';
```

## 将SQL查询结果分配给PL/SQL变量 {#h2--sql-pl-sql-}

可以使用SQL的`SELECT INTO`语句将值分配给PL/SQL变量。 对于`SELECT`列表中的每个项目，`INTO`列表中必须有一个对应的类型兼容变量。以下示例说明了这个概念。下面首先创建一个名为`CUSTOMERS`的表 -

```
CREATE TABLE CUSTOMERS( 
   ID   INT NOT NULL, 
   NAME VARCHAR (20) NOT NULL, 
   AGE INT NOT NULL, 
   ADDRESS CHAR (25), 
   SALARY   DECIMAL (18, 2),        
   PRIMARY KEY (ID) 
);
```

现在向`CUSTOMERS`表中插入一些数据记录 -

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
```

以下程序使用SQL的`SELECT INTO`子句将上表中的值分配给PL/SQL变量，

```
DECLARE 
   c_id customers.id%type := 1; 
   c_name  customerS.No.ame%type; 
   c_addr customers.address%type; 
   c_sal  customers.salary%type; 
BEGIN 
   SELECT name, address, salary INTO c_name, c_addr, c_sal 
   FROM customers 
   WHERE id = c_id;  
   dbms_output.put_line 
   ('Customer ' ||c_name || ' from ' || c_addr || ' earns ' || c_sal); 
END; 
/
```

当执行上述代码时，会产生以下结果 -

```
Customer Ramesh from Ahmedabad earns 2000  

PL/SQL procedure completed successfully
```



  


