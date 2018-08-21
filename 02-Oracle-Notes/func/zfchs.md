[返回目录](/README.md)

# 字符串函数

PL/SQL提供用于连接两个字符串的级联运算符\(`||`\)。 下表提供了PL/SQL提供的字符串函数 -

| 编号 | 函数 | 描述 |
| :--- | :--- | :--- |
| 1 | `ASCII(x);` | 返回字符`x`的ASCII值。 |
| 2 | `CHR(x);` | 返回ASCII值为`x`的字符。 |
| 3 | `CONCAT(x, y);` | 连接两个字符串`x`和`y`，并返回连接后的字符串。 |
| 4 | `INITCAP(x);` | 将`x`中每个单词的初第一个字母转换为大写，并返回该字符串。 |
| 5 | `INSTR(x, find_string [, start] [, occurrence]);` | 在`x`字符串中搜索`find_string`子串并返回找到的位置。 |
| 6 | `INSTRB(x);` | 返回字符串`x`在另一个字符串中第一次再现的位置，但返回值\(以字节为单位\)。 |
| 7 | `LENGTH(x);` | 返回`x`中的字符数，也是计算字符串的长度。 |
| 8 | `LENGTHB(x);` | 返回单字节字符集的字符串长度\(以字节为单位\)。 |
| 9 | `LOWER(x);` | 将`x`字符串中的字母转换为小写，并返回此小写字符串。 |
| 10 | `LPAD(x, width [, pad_string]) ;` | 使用空格垫放在`x`字符串的左边，以使字符串的长度达到宽度字符。 |
| 11 | `LTRIM(x [, trim_string]);` | 修剪`x`字符串左边的字符。 |
| 12 | `NANVL(x, value);` | 如果`x`匹配`NaN`特殊值\(而不是数字\)，则返回值，否则返回`x`字符串。 |
| 13 | `NLS_INITCAP(x);` | 与`INITCAP(x)`函数相同，只不过它可以使用`NLSSORT`指定的其他排序方法。 |
| 14 | `NLS_LOWER(x) ;` | 与`LOWER(x)`函数相同，除了可以使用`NLSSORT`指定的不同排序方法。 |
| 15 | `NLS_UPPER(x);` | 与`UPPER()`函数相同，除了可以使用`NLSSORT`指定的不同排序方法。 |
| 16 | `NLSSORT(x);` | 更改排序字符的方法。必须在任何`NLS()`函数之前指定; 否则，将使用默认排序。 |
| 17 | `NVL(x, value);` | 如果`x`为`null`则返回`value`值; 否则返回`x`。 |
| 18 | `NVL2(x, value1, value2);` | 如果`x`不为`null`则返回值`value1`; 如果`x`为`null`，则返回`value2`。 |
| 19 | `REPLACE(x, search_string, replace_string);` | 在`x`字符串中搜索`search_string`并将其替换为`replace_string`。 |
| 20 | `RPAD(x, width [, pad_string]);` | 使用空格垫放在`x`字符串的右边，以使字符串的长度达到宽度字符。 |
| 21 | `RTRIM(x [, trim_string]);` | 从右边修剪`x`字符串。 |
| 22 | `SOUNDEX(x) ;` | 返回一个包含`x`的语音表示的字符串。 |
| 23 | `SUBSTR(x, start [, length]);` | 返回`x`字符串从指定`start`位置开始到一个可选指定长度\(`length`\)范围内的子字符串。 |
| 24 | `SUBSTRB(x);` | 与`SUBSTR()`相同，除了参数以字节表示，还支持单字节字符系统的字符。 |
| 25 | `TRIM([trim_char FROM) x);` | 修剪`x`字符串的左边和右边的字符。 |
| 26 | `UPPER(x);` | 将`x`中的字母转换为大写，并返回此大写后的字符串。 |

现在来看下面几个例子来了解这个概念 -

**示例-1**

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   greetings varchar2(11) := 'hello world'; 
BEGIN 
   dbms_output.put_line(UPPER(greetings)); 

   dbms_output.put_line(LOWER(greetings)); 

   dbms_output.put_line(INITCAP(greetings)); 

   /* retrieve the first character in the string */ 
   dbms_output.put_line ( SUBSTR (greetings, 1, 1)); 

   /* retrieve the last character in the string */ 
   dbms_output.put_line ( SUBSTR (greetings, -1, 1)); 

   /* retrieve five characters,  
      starting from the seventh position. */ 
   dbms_output.put_line ( SUBSTR (greetings, 7, 5)); 

   /* retrieve the remainder of the string, 
      starting from the second position. */ 
   dbms_output.put_line ( SUBSTR (greetings, 2)); 

   /* find the location of the first "e" */ 
   dbms_output.put_line ( INSTR (greetings, 'e')); 
END; 
/
```

当上述代码在SQLPlus提示符下执行时，它会产生以下结果 -

```
HELLO WORLD
hello world
Hello World
h
d
world
ello world
2

PL/SQL 过程已成功完成。

Shell
```

**示例-2**

```
SET SERVEROUTPUT ON SIZE 99999;
DECLARE 
   greetings varchar2(30) := '......Hello World.....'; 
BEGIN 
   dbms_output.put_line(RTRIM(greetings,'.')); 
   dbms_output.put_line(LTRIM(greetings, '.')); 
   dbms_output.put_line(TRIM( '.' from greetings)); 
END; 
/
```

当上述代码在SQLPlus提示符下执行时，它会产生以下结果 -

![](/assets/import39.png)

