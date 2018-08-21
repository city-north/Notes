[返回目录](/README.md)

# 集合

集合分为三种类型，

* 索引表或关联数组
* 嵌套的表
* 可变大小的数组或者Varray类型

Oracle的每种类型的集合有以下特征 -

| 集合类型 | 元素个数 | 下标类型 | 密集或稀疏 | 在哪创建 | 是否为对象类型属性 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 关联数组\(或索引表\) | 无界 | 字符串或整数 | 任意一种 | 只在PL/SQL块中 | No |
| 嵌套表 | 无界 | 整数 | 开始密集，可以变得稀疏 | 在PL/SQL块或模式级别 | Yes |
| 可变大小数组\(Varray\) | 有界 | 整数 | 总是密集 | 在PL/SQL块或模式级别 | Yes |

我们已经在“PL/SQL数组”一章中讨论了varray。 在本章中，将讨论PL/SQL表。

两种类型的PL/SQL表\(即索引表和嵌套表\)具有相同的结构，并且使用下标符号来访问它们的行。 但是，这两种表在一个方面有所不同， 嵌套表可以存储在数据库列中，索引表不能。

## 索引表 {#h2-u7D22u5F15u8868}

索引表\(也称为关联数组\)是一组键 - 值对。 每个键都是唯一的，用来定位相应的值。键可以是整数或字符串。

使用以下语法创建索引表。 在这里，正在创建一个名为`table_name`的索引表，其中的键是`subscript_type`，关联的值`element_type`，参考以下语法 -

```
TYPE type_name IS TABLE OF element_type [NOT NULL] INDEX BY subscript_type; 

table_name type_name;
```

## 例子

以下示例显示了如何创建一个表来存储整数值以及名称，然后打印出相同的名称列表。

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   TYPE salary IS TABLE OF NUMBER INDEX BY VARCHAR2(20); 
   salary_list salary; 
   name   VARCHAR2(20); 
BEGIN 
   -- adding elements to the table 
   salary_list('Rajnish') := 62000; 
   salary_list('Minakshi') := 75000; 
   salary_list('Martin') := 100000; 
   salary_list('James') := 78000;  

   -- printing the table 
   name := salary_list.FIRST; 
   WHILE name IS NOT null LOOP 
      dbms_output.put_line 
      ('Salary of ' || name || ' is ' || TO_CHAR(salary_list(name))); 
      name := salary_list.NEXT(name); 
   END LOOP; 
END; 
```

```
Salary of James is 78000
Salary of Martin is 100000
Salary of Minakshi is 75000
Salary of Rajnish is 62000

PL/SQL 过程已成功完成。
```

### 实例2

索引表的元素也可以是任何数据库表的`%ROWTYPE`或任何数据库表字段的`%TYPE`

。 以下示例说明了这个概念。我们将使用存储在数据库中的`CUSTOMERS`表及数据 -

```
CREATE TABLE CUSTOMERS( 
   ID   INT NOT NULL, 
   NAME VARCHAR (20) NOT NULL, 
   AGE INT NOT NULL, 
   ADDRESS CHAR (25), 
   SALARY   DECIMAL (18, 2),        
   PRIMARY KEY (ID) 
);
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

存储过程代码：

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   CURSOR c_customers is 
      select name from customers; 

   TYPE c_list IS TABLE of customers.name%type INDEX BY binary_integer; 
   name_list c_list; 
   counter integer :=0; 
BEGIN 
   FOR n IN c_customers LOOP 
      counter := counter +1; 
      name_list(counter) := n.name; 
      dbms_output.put_line('Customer('||counter||'):'||name_list(counter)); 
   END LOOP; 
END; 
```

## 嵌套表 {#h2-u5D4Cu5957u8868}

嵌套表就像一个具有任意数量元素的一维数组。但是，嵌套表与数组在以下几个方面不同 -

* 数组是一个有声明数量的元素集合，但是一个嵌套的表没有。嵌套表的大小可以动态增加。
* 数组总是密集的，即它总是具有连续的下标。 嵌套数组最初是密集的，但是当从其中删除元素时，它可能变得稀疏。

```
TYPE type_name IS TABLE OF element_type [NOT NULL]; 

table_name type_name;
```

```
TYPE type_name IS TABLE OF element_type [NOT NULL]; 

table_name type_name;
```

这个声明类似于索引表的声明，只是没有`INDEX BY`子句。

嵌套表可以存储在数据库列中。 它可以进一步用于简化SQL操作，您可以使用更大的表来连接单列表。关联数组不能存储在数据库中。



```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   TYPE names_table IS TABLE OF VARCHAR2(10); 
   TYPE grades IS TABLE OF INTEGER;  
   names names_table; 
   marks grades; 
   total integer; 
BEGIN 
   names := names_table('Kavita', 'Pritam', 'Ayan', 'Rishav', 'Aziz'); 
   marks:= grades(98, 97, 78, 87, 92); 
   total := names.count; 
   dbms_output.put_line('Total '|| total || ' Students'); 
   FOR i IN 1 .. total LOOP 
      dbms_output.put_line('Student:'||names(i)||', Marks:' || marks(i)); 
   end loop; 
END; 
/
```

```
Total 5 Students
Student:Kavita, Marks:98
Student:Pritam, Marks:97
Student:Ayan, Marks:78
Student:Rishav, Marks:87
Student:Aziz, Marks:92

```

**示例2**

嵌套表的元素也可以是任何数据库表的`%ROWTYPE`或任何数据库表字段的`%TYPE`。以下示例说明了这个概念。我们将使用存储在数据库中的`CUSTOMERS`表，参考以下代码的实现 -

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   CURSOR c_customers is  
      SELECT  name FROM customers;  
   TYPE c_list IS TABLE of customers.name%type; 
   name_list c_list := c_list(); 
   counter integer :=0; 
BEGIN 
   FOR n IN c_customers LOOP 
      counter := counter +1; 
      name_list.extend; 
      name_list(counter)  := n.name; 
      dbms_output.put_line('Customer('||counter||'):'||name_list(counter)); 
   END LOOP; 
END; 
/
```

## 集合方法 {#h2-u96C6u5408u65B9u6CD5}

PL/SQL提供了内置的集合方法，使集合更易于使用。下表列出了方法及其用途 -

| 编号 | 方法 | 目的 |
| :--- | :--- | :--- |
| 1 | `EXISTS(n)` | 如果集合中的第`n`个元素存在，则返回`TRUE`; 否则返回`FALSE`。 |
| 2 | `COUNT` | 返回集合当前包含的元素的数量。 |
| 3 | `LIMIT` | 检查集合的最大容量\(大小\)。 |
| 4 | `FIRST` | 返回使用整数下标的集合中的第一个\(最小\)索引编号。 |
| 5 | `LAST` | 返回使用整数下标的集合中的最后\(最大\)索引编号。 |
| 6 | `PRIOR(n)` | 返回集合中索引`n`之前的索引编号。 |
| 7 | `NEXT(n)` | 返回索引`n`成功的索引号。 |
| 8 | `EXTEND` | 追加一个空\(`null`\)元素到集合。 |
| 9 | `EXTEND(n)` | 将`n`个空\(`null`\)元素追加到集合中。 |
| 10 | `EXTEND(n,i)` | 将第`i`个元素的`n`个副本追加到集合中。 |
| 11 | `TRIM` | 删除一个集合末尾的元素。 |
| 12 | `TRIM(n)` | 删除集合末尾的`n`个元素。 |
| 13 | `DELETE` | 删除集合中的所有元素，将`COUNT`设置为`0`。 |
| 14 | `DELETE(n)` | 使用数字键或嵌套表从关联数组中删除第`n`个元素。 如果关联数组有一个字符串键，则删除键值对应的元素。 如果`n`为空，则`DELETE(n)`不执行任何操作。 |
| 15 | `DELETE(m,n)` | 从关联数组或嵌套表中移除`m..n`范围内的所有元素。 如果`m`大于`n`，或者`m`或`n`为空，则`DELETE(m，n)`将不执行任何操作。 |

## 集合异常 {#h2-u96C6u5408u5F02u5E38}

下表提供了集合异常情况以及何时引发 -

| 编号 | 集合异常 | 引发的情况 |
| :--- | :--- | :--- |
| 1 | `COLLECTION_IS_NULL` | 尝试在一个原子空集合上进行操作。 |
| 2 | `NO_DATA_FOUND` | 下标指定被删除的元素或关联数组中不存在的元素。 |
| 3 | `SUBSCRIPT_BEYOND_COUNT` | 下标超出了集合中元素的数量。 |
| 4 | `SUBSCRIPT_OUTSIDE_LIMIT` | 下标超出允许的范围。 |
| 5 | `VALUE_ERROR` | 下标为空或不能转换为键类型。如果键定义为`PLS_INTEGER`范围，并且下标超出此范围，则可能会发生此异常。 |



