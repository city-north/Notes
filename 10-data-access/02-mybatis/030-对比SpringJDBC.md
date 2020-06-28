# Spring JDBC

Spring 对原生的 JDBC 进行了封装，并且给我们提供了一个 模板方法 JdbcTemplate，来简化我们对数据库的操作。

1. 我们不再需要去关心资源管理的问题。
2. 对于结果集的处理，Spring JDBC 也提供了一个 RowMapper 接口，可以把结果集转换成 Java 对象。

> 比如我们要把结果集转换成 Employee 对象，就可以针对一个 Employee
> 创建一个 RowMapper 对象，实现 RowMapper 接口，并且重写 mapRow()方法。我们 在 mapRow()方法里面完成对结果集的处理。
>
> ```java
> public class EmployeeRowMapper implements RowMapper { @Override
>     public Object mapRow(ResultSet resultSet, int i) throws SQLException {
>         Employee employee = new Employee();
>         employee.setEmpId(resultSet.getInt("emp_id"));
>         employee.setEmpName(resultSet.getString("emp_name"));
>         employee.setEmail(resultSet.getString("emial"));
>         return employee;
>     }
> }
> ```
>
> 在 DAO 层调用的时候就可以传入自定义的 RowMapper 类，最终返回我们需要的 类型。结果集和实体类类型的映射也是自动完成的。
>
> ```java
> public List<Employee> query(String sql){
> 		new JdbcTemplate( new DruidDataSource());
> 		return jdbcTemplate.query(sql,new EmployeeRowMapper());
> }
> ```
>
> 

## 帮助我们解决了一些问题:

1. 无论是 QueryRunner 还是 JdbcTemplate，都可以传入一个数据源进行初始化，也就是资源管理这一部分的事情，可以交给专门的数据源组件去做，不用 我们手动创建和关闭;
2. 对操作数据的增删改查的方法进行了封装;
3. 可以帮助我们映射结果集，无论是映射成 List、Map 还是实体类。

## 存在一些缺点

1.  SQL 语句都是写死在代码里面的，依旧存在硬编码的问题
2. 参数只能按固定位置的顺序传入(数组)，它是通过占位符去替换的，不能自动映射
3.  在方法里面，可以把结果集映射成实体类，但是不能直接把实体类映射成数据库的记录(没有自动生成 SQL 的功能);
4. 查询没有缓存的功能。

