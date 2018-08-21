[返回目录](/README.md)

# 循环

PL/SQL提供以下类型的循环来处理循环需求。可点击以下链接查看每个循环类型如何使用。

| 编号 | 循环类型 | 描述 |
| :--- | :--- | :--- |
| 1 | [PL/SQL基本LOOP循环](http://www.oraok.com/plsql/plsql_basic_loop.html) | 在这个循环结构中，语句序列包含在`LOOP`和`END LOOP`语句之间。在每次迭代时，执行语句序列，然后在循环顶部继续控制。 |
| 2 | [PL/SQL while循环](http://www.oraok.com/plsql/plsql_while_loop.html) | 当给定条件为真时，重复一个语句或一组语句。它在执行循环体之前测试状态。 |
| 3 | [PL/SQL for循环](http://www.oraok.com/plsql/plsql_for_loop.html) | 多次执行一系列语句，并缩写管理循环变量的代码。 |
| 4 | [PL/SQL嵌套循环](http://www.oraok.com/plsql/plsql_nested_loops.html) | 可在任何其他基本循环中使用一个或多个循环，如：`while`或`for`循环。 |

```

```

## 标记PL/SQL循环 {#h2--pl-sql-}

在PL/SQL中，可以标记PL/SQL循环。标签使用双尖括号\(`<<`和`>>`\)括起来，并显示在LOOP语句的开头。标签名称也可以出现在`LOOP`语句的末尾。可以使用`EXIT`语句中的标签退出循环。

以下程序说明了这个概念 -

```
SET SERVEROUTPUT ON SIZE 1000000;
DECLARE 
   i number(1); 
   j number(1); 
BEGIN 
   << outer_loop >> 
   FOR i IN 1..3 LOOP 
      << inner_loop >> 
      FOR j IN 1..3 LOOP 
         dbms_output.put_line('i is: '|| i || ' and j is: ' || j); 
      END loop inner_loop; 
   END loop outer_loop; 
END; 
/
```

![](/assets/import38.png)

## 循环控制语句 {#h2-u5FAAu73AFu63A7u5236u8BEDu53E5}

循环控制语句从其正常顺序更改执行。当执行离开范围时，在该范围内创建的所有自动对象都将被销毁。

PL/SQL支持以下控制语句。标签循环也有助于控制环外的控制。点击以下链接查看它们的详细信息。

| 编号 | 控制语句 | 描述 |
| :--- | :--- | :--- |
| 1 | [EXIT语句](http://www.oraok.com/plsql/plsql_exit_statement.html) | Exit语句完成循环，控制在`END LOOP`之后立即传递给语句。 |
| 2 | [CONTINUE语句](http://www.oraok.com/plsql/plsql_continue_statement.html) | 导致循环跳过其主体的剩余部分，并在重申之前立即重新测试其状态。 |
| 3 | [GOTO语句](http://www.oraok.com/plsql/plsql_goto_statement.html) | 转移控制到标记语句。虽然不建议在程序中使用`GOTO`语句。 |



