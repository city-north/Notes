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

