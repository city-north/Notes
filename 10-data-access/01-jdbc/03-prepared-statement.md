# PreparedStatement Example

> By Lokesh Gupta | Filed Under: [JDBC](https://howtodoinjava.com/java/jdbc/)
>
> In database management systems, a [**prepared statement**](https://en.wikipedia.org/wiki/Prepared_statement) or **parameterized statement** is a feature used to execute the same or similar database statements repeatedly with high efficiency. Typically used with SQL statements such as queries or updates, the prepared statement takes the form of a template into which certain constant values are substituted during each execution.

在数据库管理系统中,一个 prepared statement 或者是一个 parameterized statement 是一个用于, 反复高效执行相同或者类似数据库 statements ,典型的使用方式是 SQL Setements 例如一个 query 或者 update 语句, prepared statement 使用一个模板,在运行时使用具体的值替换掉

## How prepared statement works?

Most relational databases handles a JDBC / SQL query in four steps:

1. Parse the incoming SQL query 解析输入的 SQL 查询
2. Compile the SQL query 编译 SQL 查询
3. Plan/optimize the data acquisition path  计划/优化数据获取路径
4. Execute the optimized query / acquire and return data 执行优化的查询/获取和返回数据

A Statement will always proceed through the four steps above for each SQL query sent to the database. **A Prepared Statement pre-executes steps (1) – (3) in the execution process above**. Thus, when creating a Prepared Statement some pre-optimization is performed immediately. The effect is to lessen the load on the database engine at execution time.

- Statement 会在 SQL 语句每次查询时传到 database
- Prepared Statement 会预先执行 1-3 步骤,所以当我们创建一个 Prepared Statement, 这些前置优化会被立刻执行,其效果是减少数据库在执行的时候的负载

## Advantages of using prepared statement over simple JDBC statement

- Pre-compilation and DB-side caching of the SQL statement leads to overall faster execution and the ability to reuse the same SQL statement in batches.
- Automatic prevention of SQL injection attacks by builtin escaping of quotes and other special characters. Note that this requires that you use any of the PreparedStatement setXxx() methods to set the values and not use inline the values in the SQL string by string-concatenating.
- Apart from above two main usage, prepared statements makes it easy to work with complex objects like BLOBs and CLOBs.

---

- 预编译 和数据库端的SQL Statment 缓存可以有更加快的执行速度以及重用 SQL 的能力

- 自动防止SQL注入攻击内置转义的引号和其他特殊字符。注意，这需要使用任何PreparedStatement setXxx()方法来设置值，而不是通过字符串连接来内联SQL字符串中的值。
- 除了以上两种主要用法外，预处理语句还使处理诸如blob和clob之类的复杂对象变得很容易。

If you have missed, in previous posts, we have learned about types of [JDBC drivers](https://howtodoinjava.com/java/jdbc/jdbc-basics-types-of-jdbc-drivers/) and some basic operations like [making database connection using JDBC](https://howtodoinjava.com/java/jdbc/jdbc-mysql-database-connection-example/) and then how to [execute SELECT Query](https://howtodoinjava.com/misc/jdbc-select-query-example/), and then [INSET Query example](https://howtodoinjava.com/java/jdbc/jdbc-sql-insert-query-example/).

![JDBC-Icon](assets/JDBC-Icon.png)