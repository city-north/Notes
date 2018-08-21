[返回目录](/README.md)

转载 [原链接](http://www.oraok.com/plsql/plsql_data_types.html)

# 数据类型

* **标量\(SCALAR\)类型-**它是没有内部组件的单个值，如：`NUMBER`，`DATE`或`BOOLEAN`等。

* **大对象\(LOB\)类型**- 指向与其他数据项\(例如：文本，图形图像，视频剪辑和声音波形\)分开存储的大对象的指针。

* **复合类型**- 具有可单独访问的内部组件的数据项。例如，集合和记录。
* **引用类型**- 指向其他数据项。

## PL/SQL标量数据类型和子类型 {#h2-pl-sql-}

PL/SQL标量数据类型和子类型分为以下几类：

| 序号 | 类型 | 描述 |
| :--- | :--- | :--- |
| 1 | 数字 | 执行算术运算的数值。 |
| 2 | 字符 | 表示单个字符或字符串的字母数字值。 |
| 3 | 布尔 | 执行逻辑运算的逻辑值。 |
| 4 | 日期时间 | 用于表示日期和时间的值 |

### 数字类型

数字类型变量存储整数或者实数。它包含：NUMBER、PLS\__INTEGER_和_BINARY\_INTEGER_三种基本类型。

下表列出了PL/SQL预定义的数字数据类型及其子类型 -

| 序号 | 类型 | 描述 |
| :--- | :--- | :--- |
| 1 | PLS\_INTEGER | 带符号整数：`-2,147,483,648`至`2,147,483,647`，以`32`位表示 |
| 2 | BINARY\_INTEGER | 带符号整数：`-2,147,483,648`至`2,147,483,647`，以`32`位表示 |
| 3 | BINARY\_FLOAT | 单精度`IEEE 754`格式浮点数 |
| 4 | BINARY\_DOUBLE | 双精度`IEEE 754`格式浮点数 |
| 5 | NUMBER\(prec, scale\) | 在`1E-130`到\(但不包括\)`1.0E126`范围内的绝对值的定点或浮点数。`NUMBER`变量也可以表示`0` |
| 6 | DEC\(prec, scale\) | ANSI特定定点类型，最大精度为`38`位十进制数字 |
| 7 | DECIMAL\(prec, scale\) | IBM具体定点类型，最大精度为`38`位十进制数字 |
| 8 | NUMERIC\(pre, secale\) | 浮点型，最大精度为`38`位十进制数 |
| 9 | DOUBLE PRECISION | ANSI特定浮点类型，最大精度为`126`位二进制数字\(大约38位十进制数字\) |
| 10 | FLOAT | ANSI和IBM特定浮点类型，最大精度为`126`位二进制数字\(大约`38`位十进制数字\) |
| 11 | INT | ANSI特定整数类型，最大精度为`38`位十进制数 |
| 12 | INTEGER | ANSI和IBM特定整数类型，最大精度为`38`位十进制数 |
| 13 | SMALLINT | ANSI和IBM特定整数类型，最大精度为`38`位十进制数 |
| 14 | REAL | 浮点型，最大精度为`63`位二进制数字\(约十八位数\) |

以下是有效的声明 -

```
DECLARE 
   num1 INTEGER; 
   num2 REAL; 
   num3 DOUBLE PRECISION; 
BEGIN 
   null; 
END; 
/
```

## PL/SQL字符数据类型和子类型 {#h2-pl-sql-}

以下是PL/SQL预定义字符数据类型及其子类型的详细信息 -

| 序号 | 类型 | 描述 |
| :--- | :--- | :--- |
| 1 | CHAR | 固定长度字符串，最大大小为`32,767`字节 |
| 2 | VARCHAR2 | 最大大小为`32,767`字节的可变长度字符串 |
| 3 | RAW | 最大大小为`32,767`字节的可变长度二进制或字节字符串，不由PL/SQL解释 |
| 4 | NCHAR | 固定长度的国家字符串，最大大小为`32,767`字节 |
| 5 | NVARCHAR2 | 可变长度的国家字符串，最大大小为`32,767`字节 |
| 6 | LONG | 最大长度为`32,760`字节的可变长度字符串 |
| 7 | LONG RAW | 最大大小为`32,760`字节的可变长度二进制或字节字符串，不由PL/SQL解释 |
| 8 | ROWID | 物理行标识符，普通表中的行的地址 |
| 9 | UROWID | 通用行标识符\(物理，逻辑或外部行标识符\) |

## PL/SQL布尔数据类型 {#h2-pl-sql-}

`BOOLEAN`数据类型存储逻辑运算中使用的逻辑值。逻辑值为布尔值:`TRUE`,`FALSE`以及`NULL`值。

但是，SQL没有类似于`BOOLEAN`的数据类型。 因此，布尔值不能用于 -

* SQL语句
* 内置SQL函数\(如:`TO_CHAR`\)
* 从SQL语句调用PL/SQL函数

## PL/SQL日期时间和间隔类型 {#h2-pl-sql-}

`DATE`数据类型用于存储固定长度的数据日期时间，其包括自午夜以来以秒为单位的时间。 有效期为公元前4712年1月1日至公元9999年12月31日。

默认日期格式由Oracle初始化参数`NLS_DATE_FORMAT`设置。 例如，默认值可能是`“DD-MON-YY”`，其中包括一个月份的两位数字，月份名称的缩写以及年份的最后两位数字。 例如，`01-OCT-12`。

每个`DATE`类型的数据值包括世纪，年，月，日，时，分，秒。下表显示每个字段的有效值 -

| 字段名 | 有效的日期时间值 | 有效间隔值 |
| :--- | :--- | :--- |
| YEAR | `-4712`至`9999`\(不包括第`0`年\) | 任意非零整数 |
| MONTH | `01`~`12` | `01`~`11` |
| DAY | `01`至`31`\(限于`MONTH`和`YEAR`的值，根据本地日历的规则\) | 任何非零整数 |
| HOUR | `00`~`23` | `00`~`23` |
| MINUTE | `00`~`59` | `00`~`59` |
| SECOND | `00`~`59.9(n)`，其中`9(n)`是时间分秒的精度 | `00`~`59.9(n)`，其中`9(n)`是间隔分数秒的精度 |
| TIMEZONE\_HOUR | `-12`至`14`\(范围适应夏令时更改\) | 不适用 |
| TIMEZONE\_MINUTE | `00`~`59` | 不适用 |
| TIMEZONE\_REGION | 在动态性能视图`V$TIMEZONE_NAMES`找到 | 不适用 |
| TIMEZONE\_ABBR | 在动态性能视图`V$TIMEZONE_NAMES`找到 | 不适用 |

## PL/SQL大对象\(LOB\)数据类型 {#h2-pl-sql-lob-}

大对象\(LOB\)数据类型指的是大数据项，如文本，图形图像，视频剪辑和声音波形。 LOB数据类型允许对数据进行高效，随机，分段访问。以下是预定义的PL/SQL LOB数据类型 -

| 数据类型 | 描述 | 大小 |
| :--- | :--- | :--- |
| BFILE | 用于在数据库外的操作系统文件中存储大型二进制对象。 | 取决于系统，但不得超过`4GB`。 |
| BLOB | 用于在数据库中存储的大型二进制对象 | `8TB`至`128TB` |
| CLOB | 用于在数据库中存储大字符数据。 | `8TB`至`128TB` |
| NCLOB | 用于在数据库中存储大块`NCHAR`数据。 | `8TB`至`128TB` |

## PL/SQL用户定义的子类型 {#h2-pl-sql-}

子类型是另一种数据类型的子集，它称为基本类型。子类型具有与其基本类型相同的操作，但只有基本类型有效值的子集。

PL/SQL预定义包`STANDARD`中的几个子类型。 例如，PL/SQL预先定义子类型`CHARACTER`和`INTEGER`，如下所示：

```
SUBTYPE CHARACTER IS CHAR;
 
SUBTYPE INTEGER IS NUMBER(38,0);
```

可以定义和使用自己的子类型。以下程序说明了如何定义和使用用户定义的子类型 -

```
DECLARE
   SUBTYPE name IS char(20);
   SUBTYPE message 
IS
 varchar2(100);  
 salutation name;
 greetings message;
BEGIN
   salutation :='Reader ';
   greetings :='Welcome to the World of PL/SQL';
    dbms_output.put_line('Hello '|| salutation || greetings);
END;

```

当上述代码在SQL提示符下执行时，它会产生以下结果 -

```
Hello Reader Welcome to the World of PL/SQL 

PL/SQL procedure successfully completed.
```

## PL/SQL中的NULL {#h2-pl-sql-null}

PL/SQL中的`NULL`值表示丢失或未知数据，它们不是整数，字符或任何其他特定数据类型。 请注意，`NULL`与空数据字符串或空字符值`“\0”`不同。可以将一个`null`值分配给其它变量，但不能等同于任何东西，包括其自身\(`null`\)。

