# Quick Start

## 为什么要有 Mybatis

- 为了将业务逻辑与数据逻辑相分离

- 将 Java 的方法与 SQL语法关联

#### 传统的 JDBC 查询数据库的弊端

```java
/**
 * 传统 JDBC方式查询数据库的弊端很多
 *
 * @throws IOException
 */
@Test
public void testJdbc() throws IOException {
    Connection conn = null;
    Statement stmt = null;
    Blog blog = new Blog();

    try {
        // 注册 JDBC 驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 打开连接
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/notes", "root", "root");

        // 执行查询
        stmt = conn.createStatement();
        String sql = "SELECT bid, name, author_id FROM blog";
        ResultSet rs = stmt.executeQuery(sql);

        // 获取结果集
        while (rs.next()) {
            Integer bid = rs.getInt("bid");
            String name = rs.getString("name");
            Integer authorId = rs.getInt("author_id");
            blog.setAuthorId(authorId);
            blog.setBid(bid);
            blog.setName(name);
        }
        System.out.println(blog);

        rs.close();
        stmt.close();
        conn.close();
    } catch (SQLException se) {
        se.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException se2) {
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
```

- 业务代码与数据库操作代码耦合,不利于维护
- 每一段代码都需要自己去管理数据库的链接,如果忘记关闭链接,可能会造成数据库服务的链接耗尽

#### SpringJDBC

```java
/**
 * <p>
 * SpringJDbc
 * </p>
 *
 * @author EricChen 2020/02/21 20:44
 */
public class SpringJdbcTest {
    private static final String PROPERTY_PATH = "/hikari.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtilsTest.class);
    private static HikariDataSource dataSource;
    private static QueryRunner queryRunner;

    static {
        HikariConfig config = new HikariConfig(PROPERTY_PATH);
        dataSource = new HikariDataSource(config);
        queryRunner = new QueryRunner(dataSource);
    }


    @Test
    public void testSpringJdbc() {
        DbUtilsTest.init();
        String sql = "select * from blog";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Blog> query = jdbcTemplate.query(sql, new BlogMapper());
        System.out.println(query);
    }


}

public class BlogMapper implements RowMapper<Blog> {
    @Override
    public Blog mapRow(ResultSet rs, int rowNum) throws SQLException {
        Blog blog = new Blog();
        blog.setAuthorId(rs.getInt("bid"));
        return blog;
    }
}

```

无论是 DbUtils 还是 SpringJDBC,他们仅仅是在 JDBC 的基础上做了轻量级的封装,但是

- Sql 语句依然需要写死在代码中
- 参数只能按照固定的位置的顺序传入,它是通过占位符去替换的,不能自动映射
- 在方法里面,可以把结果集映射成实体类,但是不能直接把实体类映射成数据库的记录
- 查询没有缓存的功能

#### ORM

ORM 是 Object Relational Mapping , 对象关系映射,实际上就是 Java 对象和关系型数据库进行了互相映射的关系

hibernate 是一种 ORM框架,通过配置对象与数据库的关系,可以做到自动映射,但是也存在一定的问题:

- 比如 get , save, update 对象的方法,操作的是所有字段,没有办法指定部分字段,不够灵活
- 性能比较差,因为是自动生成 SQL,所以需要对框架非常熟悉才能进行优化
- 不支持动态 SQL,比如分表中的表名变化,以及条件和参数

#### 引出 Mybatis

Mybatis 比较 Hibernate 相对来说是"半自动的",封装程度没有 Hibernate 这么高,就像半自动咖啡机,有更多的用户可以进行的操作,从而提高了灵活性

在 Mybatis 中,SQL 与代码是分离的,所以基本上来说会写 SQL 就 会写 Mybatis

#### Mybatis 的核心特性

- 使用连接池对链接进行管理
- SQL 和代码分离,集中管理
- 结果集映射
- 重复 SQL 的提取
- 缓存管理
- 插件机制

Mybatis 也是对 JDBC的封装,到底层,也是使用的 Statement 作为查询, ResultSet 作为结果集

## 实例

#### 准备

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/notes?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```

#### 使用Statement 查询

每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先定制的 Configuration 的实例构建出 SqlSessionFactory 的实例。

从 XML 文件中构建 SqlSessionFactory 的实例非常简单，建议使用类路径下的资源文件进行配置。 但是也可以使用任意的输入流（InputStream）实例，包括字符串形式的文件路径或者 file:// 的 URL 形式的文件路径来配置。MyBatis 包含一个名叫 Resources 的工具类，它包含一些实用方法，可使从 classpath 或其他位置加载资源文件更加容易。

```java
 /**
     * 直接使用 statement 的 id进行查询
     */
    @Test
    public void testStatement() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            User user = session.selectOne("vip.ericchen.study.mybatis.UserMapper.selectUser", 1);
            System.out.println(user);
        }
    }
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="vip.ericchen.study.mybatis.UserMapper">
    <select id="selectUser" resultType="vip.ericchen.study.mybatis.User">
    select * from User where id = #{id}
  </select>
</mapper>
```

#### 不使用 XML 构建 SqlSessionFactory

如果你更愿意直接从 Java 代码而不是 XML 文件中创建配置，或者想要创建你自己的配置构建器，MyBatis 也提供了完整的配置类，提供所有和 XML 文件相同功能的配置项。

```java
DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
TransactionFactory transactionFactory = new JdbcTransactionFactory();
Environment environment = new Environment("development", transactionFactory, dataSource);
Configuration configuration = new Configuration(environment);
configuration.addMapper(BlogMapper.class);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
```

注意该例中，configuration 添加了一个映射器类（mapper class）。映射器类是 Java 类，它们包含 SQL 映射语句的注解从而避免依赖 XML 文件。不过，由于 Java 注解的一些限制以及某些 MyBatis 映射的复杂性，要使用大多数高级映射（比如：嵌套联合映射），仍然需要使用 XML 配置。有鉴于此，如果存在一个同名 XML 配置文件，MyBatis 会自动查找并加载它（在这个例子中，基于类路径和 BlogMapper.class 的类名，会加载 BlogMapper.xml）。

```java
    /**
     * 测试使用 Mapper 类的方式查询
     * @throws IOException
     */
    @Test
    public void testSelect() throws IOException  {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.selectUser(1);
            System.out.println(user);
        }
    }
```

