[返回目录](/README.md)

# 存储过程

子程序可以被称为调用程序的另一个子程序或程序调用。

可以在以下几个地方中创建一个子程序 -

* **在模式\(schema\)级别**中，子程序是一个独立的子程序。它是使用`CREATE PROCEDURE`或`CREATE FUNCTION`语句创建的。它存储在数据库中，可以使用`DROP PROCEDURE`或`DROP FUNCTION`语句进行删除。
* **一个程序包中**，它存储在数据库中，只有当使用`DROP PACKAGE`语句删除程序包时，才能将其删除
* **在PL/SQL块中**
  * **函数** - 这些子程序返回单个值; 主要用于计算和返回值。
  * **存储过程\(程序\) **- 这些子程序不直接返回值; 主要用于执行动作。

## 创建存储过程语法

```
CREATE [OR REPLACE] PROCEDURE procedure_name 
[(parameter_name [IN | OUT | IN OUT] type [, ...])] 
{IS | AS} 
BEGIN 
  < procedure_body > 
END procedure_name;
```

其中，

* _procedure-name_是要创建的存储过程的名称。
* _\[OR REPLACE\]_选项允许修改现有的过程。
* 可选参数列表包含参数的名称，模式和类型。`IN`表示将从外部传递的值，`OUT`表示将用于返回过程外的值的参数。
* _procedure-body_包含可执行部分。
* 使用`AS`关键字而不是`IS`关键字来创建存储过程。

## 例子

```
SET SERVEROUTPUT ON SIZE 99999;
CREATE OR REPLACE PROCEDURE greetings 
AS 
BEGIN 
   dbms_output.put_line('Hello World!'); 
END; 
/

-- 执行存储过程
exec greetings;
-- 或者
EXECUTE greetings;
```

## 调用

独立的存储程序可以通过两种方式调用 -

* 使用`EXECUTE`关键字
* 从PL/SQL块调用过程的名称

```
EXECUTE greetings;
```

```
BEGIN 
   greetings; 
END; 
/
```

## 删除存储过程

使用`DROP PROCEDURE`语句删除独立存储过程。删除程序的语法是 -

```
DROP PROCEDURE procedure-name;
```

可以使用以下语句删除`greetings`存储过程程序 -

```
DROP PROCEDURE greetings;
```

## PL/SQL子程序中的参数模式 {#h2-pl-sql-}

下表列出了PL/SQL子程序中的参数模式 -

| 编号 | 参数模式 | 描述 |
| :--- | :--- | :--- |
| 1 | `IN` | `IN`参数允许将值传递给子程序。它是一个只读参数。在子程序中，`IN`参数的作用如常数，它不能被赋值。可以将常量，文字，初始化的变量或表达式作为`IN`参数传递。也可以将其初始化为默认值; 然而，在这种情况下，从子程序调用中省略它。 它是参数传递的默认模式。参数通过引用传递。 |
| 2 | `OUT` | `OUT`参数返回一个值给调用程序。在子程序中，`OUT`参数像变量一样。 可以更改其值并在分配该值后引用该值。实际参数必须是可变的，并且通过值传递。 |
| 3 | `IN OUT` | `IN OUT`参数将初始值传递给子程序，并将更新的值返回给调用者。 它可以分配一个值，该值可以被读取。对应于`IN OUT`形式参数的实际参数必须是变量，而不是常量或表达式。正式参数必须分配一个值。实际参数\(实参\)通过值传递。 |

**IN和OUT模式 - 示例1**

假设以下存储过程需要求出两个值中的最小值。这里，存储过程两个输入的数字使用`IN`模式，并使用`OUT`模式参数返回最小值。

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   a number; 
   b number; 
   c number;
PROCEDURE findMin(x IN number, y IN number, z OUT number) IS 
BEGIN 
   IF x < y THEN 
      z:= x; 
   ELSE 
      z:= y; 
   END IF; 
END;   
BEGIN 
   a:= 12; 
   b:= 35; 
   findMin(a, b, c); 
   dbms_output.put_line('两个数：12, 35中的最小值是 : ' || c); 
END; 
/
```

当上述代码在SQL提示符下执行时，它会产生以下结果 -

```
两个数：12, 35中的最小值是 : 12
```

**IN和OUT模式 - 示例2**

此过程计算传递值的值的平方。此示例显示了如何使用相同的参数来接受值，然后返回另一个结果。

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   a number; 
PROCEDURE squareNum(x IN OUT number) IS 
BEGIN 
  x := x * x; 
END;  
BEGIN 
   a:= 11; 
   squareNum(a); 
   dbms_output.put_line(' Square of (23): ' || a); 
END; 
/
```

## 传递参数的方法 {#h2-u4F20u9012u53C2u6570u7684u65B9u6CD5}

实际参数\(实参\)可以通过三种方式传递 -

* 位置符号
* 命名符号
* 混合符号

**位置符号**

在位置符号中，可以调用存储过程如下 -

```
findMin(a, b, c, d);
```

在位置符号中，第一个实际参数代替第一个形式参数; 第二个实际参数代替第二个形式参数，依此类推。 因此，`a`代替`x`，`b`代替`y`，`c`代替`z`，`d`代替`m`。

**命名符号**

在命名符号中，实际参数与使用箭头符号\(`=>`\)的形式参数相关联。调用存储过程如下所示 -

```
findMin(x => a, y => b, z => c, m => d);
```

**混合符号**

在混合符号表示中，可以在过程调用中混合使用符号; 然而，位置符号应在命名符号之前。

以下调用存储过程的方式是合法的 -

```
findMin(a, b, c, m => d);
```

但是，以下这种是不合法的：

```
findMin(x => a, b, c, d);
```



