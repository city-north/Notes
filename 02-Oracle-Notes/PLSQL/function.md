[返回目录](/README.md)

## 函数

## 创建函数

语法：

```
CREATE [OR REPLACE] FUNCTION function_name 
[(parameter_name [IN | OUT | IN OUT] type [, ...])] 
RETURN return_datatype 
{IS | AS} 
BEGIN 
   < function_body > 
END [function_name];
```

其中，

* _function-name_是指定要创建的函数的名称。
* _\[OR REPLACE\]_选项指示是否允许修改现有的函数。
* 可选参数列表包含参数的名称，模式和类型。`IN`表示将从外部传递的值，`OUT`表示将用于返回过程外的值的参数。
* 函数必须包含一个返回\(`RETURN`\)语句。
* _RETURN_子句指定要从函数返回的数据类型。
* _function-body_包含可执行部分。
* 使用`AS`关键字代替`IS`关键字，用来创建独立的函数。

## 例子

建表语句

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

## 创建

基于上面表和数据记录，创建一个函数：_totalCustomers_来计算总客户数量。参考以下代码 -

```
CREATE OR REPLACE FUNCTION totalCustomers 
RETURN number IS 
   total number(2) := 0; 
BEGIN 
   SELECT count(*) into total 
   FROM customers; 

   RETURN total; 
END; 
/
```

## 调用

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   c number(2); 
BEGIN 
   c := totalCustomers(); 
   dbms_output.put_line('当前客户的总数为: ' || c); 
END; 
```

## 递归函数

计算一个阶乘

```
n! = n*(n-1)! 
   = n*(n-1)*(n-2)! 
      ... 
   = n*(n-1)*(n-2)*(n-3)... 1
```



```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   num number; 
   factorial number;  

FUNCTION fact(x number) 
RETURN number  
IS 
   f number; 
BEGIN 
   IF x=0 THEN 
      f := 1; 
   ELSE 
      f := x * fact(x-1); 
   END IF; 
RETURN f; 
END;  

BEGIN 
   num:= 10; 
   factorial := fact(num); 
   dbms_output.put_line(' 数字 '|| num || ' 的阶乘积是： ' || factorial); 
END; 
/
```



