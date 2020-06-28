# 翻页

在写存储过程的年代，翻页也是一件很难调试的事情，我们要实现数据不多不少准确地返回，需要大量的调试和修改。但是如果自己手写过分页，就能清楚分页的原理。 

#### 逻辑翻页与物理翻页

在我们查询数据库的操作中，有两种翻页方式，一种是逻辑翻页(假分页)，一种是物理翻页(真分页)。

逻辑翻页的原理是把所有数据查出来，在内存中删选数据。 物理翻页是真正的翻页，比如 MySQL 使用 limit 语句，Oracle 使用 rownum 语句，SQL Server 使用 top 语句。

##### 逻辑翻页

MyBatis 里面有一个逻辑分页对象 RowBounds，里面主要有两个属性，offset 和 limit(从第几条开始，查询多少条)。我们可以在 Mapper 接口的方法上加上这个参数，不需要修改 xml 里面的 SQL 语句。

```java
    /**
     * 逻辑分页
     * @throws IOException
     */
    @Test
    public void testSelectByRowBounds() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            int start = 0; // offset
            int pageSize = 5; // limit
            RowBounds rb = new RowBounds(start, pageSize);
            List<Blog> list = mapper.selectBlogList(rb); // 使用逻辑分页
            for(Blog b :list){
                System.out.println(b);
            }
        } finally {
            session.close();
        }
    }
```

它的底层其实是对 ResultSet 的处理。它会舍弃掉前面 offset 条数据，然后再取剩下的数据的 limit 条

```java
  private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping)
      throws SQLException {
    DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
    ResultSet resultSet = rsw.getResultSet();
    skipRows(resultSet, rowBounds);
    while (shouldProcessMoreRows(resultContext, rowBounds) && !resultSet.isClosed() && resultSet.next()) {
      ResultMap discriminatedResultMap = resolveDiscriminatedResultMap(resultSet, resultMap, null);
      Object rowValue = getRowValue(rsw, discriminatedResultMap, null);
      storeObject(resultHandler, resultContext, rowValue, parentMapping, resultSet);
    }
  }
```

> 很明显，如果数据量大的话，这种翻页方式效率会很低(跟查询到内存中再使用 subList(start,end)没什么区别)。所以我们要用到物理翻页。

##### 物理翻页

物理翻页是真正的翻页，它是通过数据库支持的语句来翻页。 

第一种简单的办法就是传入参数(或者包装一个 page 对象)，在 SQL 语句中翻页。

```java
    <select id="selectBlogPage" parameterType="map" resultMap="BaseResultMap">
        select * from blog limit #{curIndex} , #{pageSize}
    </select>
```

- 第一个问题是我们要在 Java 代码里面去计算起止序号;

- 第二个问题是:每个需要翻 页的 Statement 都要编写 limit 语句，会造成 Mapper 映射器里面很多代码冗余。

那我们就需要一种通用的方式，不需要去修改配置的任何一条 SQL 语句，只要在我 们需要翻页的地方封装一下翻页对象就可以了。

我们最常用的做法就是使用翻页的插件，这个是基于 MyBatis 的拦截器实现的，比 如 PageHelper。

# PageHelper

| 对象            | 作用         |
| --------------- | ------------ |
| PageInterceptor | 自定义拦截器 |
| Page            | 包装分页参数 |
| PageInfo        | 包装结果     |
| PageHelper      | 工具类       |

(基于 spring-mybatis)PageInterceptor 类

1、用法(EmployeeController. getEmpsWithJson())

```java
PageHelper.startPage(pn, 10);
List<Employee> emps = employeeService.getAll();
PageInfo page = new PageInfo(emps, 10);
```

先看 PageHelper jar 包中 PageInterceptor 的源码。拦截的是 Executor 的两个 query()方法。在这里对 SQL 进行了改写:

```java
  //调用方言获取分页 sql
String pageSql = dialect.getPageSql(ms, boundSql, parameter, rowBounds, pageKey);
```

跟踪到最后，是在` MySqlDialect. getPageSql()`对 SQL 进行了改写，翻页参数是从 一个 Page 对象中拿到的，那么 Page 对象是怎么传到这里的呢?
上一步，`AbstractHelperDialect. getPageSql()`中:

```
Page page = getLocalPage();
return getPageSql(sql, page, pageKey);
```

Page 对象是从一个 ThreadLocal<>变量中拿到的，那它是什么时候赋值的?

回到 EmployeeController. getEmpsWithJson()中，`PageHelper.startPage()`方法， 把分页参数放到了 ThreadLocal<>变量中。

```
protected static void setLocalPage(Page page) { LOCAL_PAGE.set(page);
}
```

```java
    @Test
    public void testPageHelper() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            PageHelper.startPage(2, 1);
            List<AuthorAndBlog> authorAndBlogs = mapper.selectAuthorWithBlog();
            System.out.println(authorAndBlogs);
        } finally {
            session.close();
        }
    }
```

