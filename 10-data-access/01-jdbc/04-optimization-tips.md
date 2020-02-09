# JDBC 性能优化

JDBC performance Optimization Tips

> [**Java database connectivity (JDBC)**](https://howtodoinjava.com/category/java/jdbc/) is the JavaSoft specification of a standard application programming interface (API) that allows Java programs to access database management systems. The JDBC API consists of a set of interfaces and classes written in the Java programming language. Using these standard interfaces and classes, programmers can write applications that connect to databases, send queries written in structured query language (SQL), and process the results. JDBC is oriented towards relational databases.
>
> Though it is not standard anymore to use JDBC directly into your application because we got many more robust APIs to do this job for us e.g. [**hibernate**](https://howtodoinjava.com/hibernate-tutorials/) and [**iBatis**](https://howtodoinjava.com/ibatis/). But, if you are still struck in there due to specific requirements or simply you are learning it, then below suggestions will help you in writing more fast and efficient code.

- Use Object Pooling 
- Consider MetaData Performance
- Choose Commit Mode carefully
- Save Some Bytes On NetWork Traffic

## **Use Object Pooling Almost Always**

总要想起使用对象池

- Connection Pooling  
- Statement Pooling 

### Connection Pooling 

DataBase connection are often expensive to create beacuse of the overhead [经常性支出] of establishing a network connection and initializing a database connection session on the back end database. In turn, connection session initialization often requires time comsuming processing to perform user authentication . establish transactional context and establish other aspects of the session are required for subsequent database usage.

Additionally, the database’s ongoing management of all of its connection sessions can impose a major limiting factor on the scalability of your application. Valuable database resources such as locks, memory, cursors, transaction logs, statement handles and temporary tables all tend to increase based on the number of concurrent connection sessions.

另外,数据库对其所有连接会话的持续管理可能会对应用程序的可伸缩性造成重大限制。

当 connection sessions 增加时, 许多数据库的资源例如, locks, memory, cursors, transaction logs, statement handles and temporary table 都会增加

**Enabling Connection pooling allows the pool manager to keep connections in a “pool” after they are closed**. The next time a connection is needed, if the connection options requested match one in the pool then that connection is returned instead of incurring the overhead of establishing another actual socket connection to the server.

**启用连接池允许池管理器在连接关闭后将它们保持在“池”中**。下一次需要连接时，如果请求的连接选项与池中的连接选项匹配，那么将返回该连接，而不会导致建立到服务器的另一个实际 socket 连接的开销。

### Statement Pooling

Starting from JDBC 3.0, JDBC standards define a statement-caching interface. Setting the MaxPooledStatements connection option enables statement pooling. **Enabling statement pooling allows the driver to re-use Prepared Statement objects**. When Prepared Statements are closed they are returned to the pool instead of being freed and the next Prepared Statement with the same SQL statement is retrieved from the pool rather than being instantiated and prepared against the server.

Statement caching can do the following:

1. Prevent the overhead of repeated cursor creation 避免重复索引创建的性能支出
2. Prevent repeated statement parsing and creation 避免重复 Statement 解析和创建的支出
3. Reuse data structures in the client 重用客户端中的数据结构

Please ensure your driver support this feature, and is enabled by default or not. A sample code can be something like this if you are doing it programatically.

```
Properties p = new Properties();
p.setProperty("user", "root");
p.setProperty("password", "password");
p.setProperty("MaxPooledStatements", "200");
 
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBCDemo", p);
```

### **Consider MetaData Performance Also**

If you are dealing with metadata into your code then it is another important area to look after. Here the first tip is to **use as many parameters (or filters) to fetch metadata as much you can** specify. For example, don’t call getTables like this:

> 在处理元数据的时候要格外的注意
>
> 第一个技巧是尽量使用查询参数(或者是过滤器) 去获取 metadata 

```java
DatabaseMetaData dbmd = connection.getMetaData();
ResultSet rs = dbmd.getTables(null,null,null,null);
```

Specifying at least the schema will avoid returning information on all tables for every schema when the request is sent to the server:

> 指定一个 schema ,以避免他查询多个 schema 的表

```
DatabaseMetaData dbmd = connection.getMetaData();
ResultSet rs = dbmd.getTables(null,"testDB",null,null);
```

Secondly, remember that **most JDBC drivers populate the ResultSetMetaData object at fetch time** when the needed data is returned in select queries. Use this information instead of getting data from DatabaseMetaData which is addionational request and avoidable in most cases.

> 其次，请记住，当在select查询中返回所需的数据时，大多数JDBC驱动程序在获取时填充`ResultSetMetaData`对象。使用这些信息，而不是从`DatabaseMetaData`获取数据，这是额外的要求，在大多数情况下可以避免。

```
selectStmt = connection.createStatement();
ResultSet rs = selectStmt.executeQuery("SELECT ID,FIRST_NAME,LAST_NAME,STAT_CD FROM EMPLOYEE WHERE ID <= 10");
 
ResultSetMetaData rsmd = rs.getMetaData();
rsmd.getColumnCount();
rsmd.getColumnName(0);
rsmd.getColumnType(0);
rsmd.getColumnTypeName(0);
rsmd.getColumnDisplaySize(0);
rsmd.getPrecision(0);
rsmd.getScale(0);
```

 Instead of using getColumns to get data about a table, consider issuing a dummy query and using the returned ResultSetMetaData which avoids querying the system tables!

## **Choose Commit Mode carefully**

When writing a JDBC application, make sure you consider how often you are committing transactions. Every commit causes the driver to send packet requests over the socket. Additionally, the database performs the actual commit which usually entails disk I/O on the server. **Consider removing auto-commit mode for your application** and using manual commit instead to better control commit logic.

Code to use is:

```
Connection.setAutoCommit(false);
```

## Save Some Bytes On Network Traffic

To reduce network traffic , following suggestions can be looked and adapted in appropriate for your application .

1. **Use addBatch() instead of using Prepared Statement to insert data when working with high volume clients**. This sends multiple insert requests in a single network packet and save some bytes for you.
2. **Do not use “select \* from table”**. Instead specify column names which are actually needed. I will suggest that make it a practice because many times we are doing it without realizing it’s negative impacts. Just imagine if you do this in a table where you are storing BLOBs also. You fetch such heavy objects from database and do not use it. What a waste.
3. **Ensure that your database is set to the maximum packet size** and that the driver matches that packet size. For fetching larger result sets, this reduces the number of total packets sent/received between the driver and server.