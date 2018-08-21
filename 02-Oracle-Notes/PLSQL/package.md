[返回目录](/README.md)

# 程序包

包是一个逻辑概念，它将相关的PL/SQL类型，变量和子程序分组。

一个包将会有两个程序强制性部分

* 包规范/格式
* 包体和定义

## 测试用的

```
CREATE TABLE CUSTOMERS( 
   ID   INT NOT NULL, 
   NAME VARCHAR (20) NOT NULL, 
   AGE INT NOT NULL, 
   ADDRESS CHAR (25), 
   SALARY   DECIMAL (18, 2),        
   PRIMARY KEY (ID) 
);  

-- 数据
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

## 包规范

规范是包的接口。它只是生命可以从包外部引用的类型，变量，常量，异常，游标和子程序。他**包含了有关包的所有信息，但是不包括子程序的代码。**

所有放在规范中的对象被称为**公共对象。**任何不在包规范中但是在包体中编码的子程序称为私有对象。

声明一个包含单个过程的包规范：

```
CREATE PACKAGE cust_sal AS 
   PROCEDURE find_sal(c_id customers.id%type); 
END cust_sal;
```

## 包体

包体具有包规范中声明的各种代码和其他声明，这些声明对包之外的代码是**隐藏**的。

```
CREATE OR REPLACE PACKAGE BODY cust_sal AS  

   PROCEDURE find_sal(c_id customers.id%TYPE) IS 
   c_sal customers.salary%TYPE; 
   BEGIN 
      SELECT salary INTO c_sal 
      FROM customers 
      WHERE id = c_id; 
      dbms_output.put_line('Salary: '|| c_sal); 
   END find_sal; 
END cust_sal;
```

CREATE PACKAGE BODY 语句用于创建包体。以上代码片段显示了上面创建cust\_sal包的包体声明。

## 使用包元素

包元素（变量，过程或者函数）使用下面的语法来访问的

```
package_name.element_name;
```

考虑一下，假设已经在数据库模式中创建了上面的包，下面的程序中需要调用

`cust_sal`包中的`find_sal`方法 -



```
DECLARE 
   code customers.id%type := &cc_id; 
BEGIN 
   cust_sal.find_sal(code); 
END; 
```

## 例子：

**包规范**

```
SET SERVEROUTPUT ON SIZE 99999;
CREATE OR REPLACE PACKAGE c_package AS 
   -- Adds a customer 
   PROCEDURE addCustomer(c_id customers.id%TYPE, 
   c_name  customers.name%TYPE, 
   c_age  customers.age%TYPE, 
   c_addr customers.address%TYPE,  
   c_sal  customers.salary%TYPE); 

   -- Removes a customer 
   PROCEDURE delCustomer(c_id  customers.id%TYPE); 
   --Lists all customers 
   PROCEDURE listCustomer; 

END c_package;
```

**创建程序包体**

```
CREATE OR REPLACE PACKAGE BODY c_package AS 
   PROCEDURE addCustomer(c_id  customers.id%type, 
      c_name customers.name%type, 
      c_age  customers.age%type, 
      c_addr  customers.address%type,  
      c_sal   customers.salary%type) 
   IS 
   BEGIN 
      INSERT INTO customers (id,name,age,address,salary) 
         VALUES(c_id, c_name, c_age, c_addr, c_sal); 
   END addCustomer; 

   PROCEDURE delCustomer(c_id   customers.id%type) IS 
   BEGIN 
      DELETE FROM customers 
      WHERE id = c_id; 
   END delCustomer;  

   PROCEDURE listCustomer IS 
   CURSOR c_customers is 
      SELECT  name FROM customers; 
   TYPE c_list is TABLE OF customers.name%type; 
   name_list c_list := c_list(); 
   counter integer :=0; 
   BEGIN 
      FOR n IN c_customers LOOP 
      counter := counter +1; 
      name_list.extend; 
      name_list(counter) := n.name; 
      dbms_output.put_line('Customer(' ||counter|| ')'||name_list(counter)); 
      END LOOP; 
   END listCustomer;

END c_package; 
```

**使用程序包**

```
DECLARE 
   code customers.id%type:= 8; 
BEGIN 
   c_package.addcustomer(7, 'Andy Liu', 25, 'Chennai', 3500); 
   c_package.addcustomer(8, 'Kobe Bryant', 32, 'Delhi', 7500); 
   c_package.listcustomer; 
   c_package.delcustomer(code); 
   c_package.listcustomer; 
END; 
```

```
Old salary:
New salary: 3500
Salary difference:
Old salary:
New salary: 7500
Salary difference:
Customer(1)Ramesh
Customer(2)Khilan
Customer(3)kaushik
Customer(4)Chaitali
Customer(5)Hardik
Customer(6)Komal
Customer(7)Andy Liu
Customer(8)Kobe Bryant
Customer(1)Ramesh
Customer(2)Khilan
Customer(3)kaushik
Customer(4)Chaitali
Customer(5)Hardik
Customer(6)Komal
Customer(7)Andy Liu

PL/SQL 过程已成功完成。
```



