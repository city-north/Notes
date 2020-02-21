## 动态 SQL

MyBatis 的强大特性之一便是它的动态 SQL。如果你有使用 JDBC 或其它类似框架的经验，你就能体会到根据不同条件拼接 SQL 语句的痛苦。例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL 这一特性可以彻底摆脱这种痛苦。

虽然在以前使用动态 SQL 并非一件易事，但正是 MyBatis 提供了可以被用在任意 SQL 映射语句中的强大的动态 SQL 语言得以改进这种情形。

动态 SQL 元素和 JSTL 或基于类似 XML 的文本处理器相似。在 MyBatis 之前的版本中，有很多元素需要花时间了解。MyBatis 3 大大精简了元素种类，现在只需学习原来一半的元素便可。MyBatis 采用功能强大的基于 OGNL 的表达式来淘汰其它大部分元素。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

### if

动态 SQL 通常要做的事情是根据条件包含 where 子句的一部分。比如：

```xml
<select id="findActiveBlogWithTitleLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
</select>
```

这条语句提供了一种可选的查找文本功能。如果没有传入“title”，那么所有处于“ACTIVE”状态的BLOG都会返回；反之若传入了“title”，那么就会对“title”一列进行模糊查找并返回 BLOG 结果（细心的读者可能会发现，“title”参数值是可以包含一些掩码或通配符的）。

如果希望通过“title”和“author”两个参数进行可选搜索该怎么办呢？首先，改变语句的名称让它更具实际意义；然后只要加入另一个条件即可。

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```



### choose, when, otherwise

有时我们不想应用到所有的条件语句，而只想从中择其一项。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

还是上面的例子，但是这次变为提供了“title”就按“title”查找，提供了“author”就按“author”查找的情形，若两者都没有提供，就返回所有符合条件的 BLOG（实际情况可能是由管理员按一定策略选出 BLOG 列表，而不是返回大量无意义的随机结果）。

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```



### trim, where, set

前面几个例子已经合宜地解决了一个臭名昭著的动态 SQL 问题。现在回到“if”示例，这次我们将“ACTIVE = 1”也设置成动态的条件，看看会发生什么。

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE
  <if test="state != null">
    state = #{state}
  </if>
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

如果这些条件没有一个能匹配上会发生什么？最终这条 SQL 会变成这样：

```xml
SELECT * FROM BLOG
WHERE
```

这会导致查询失败。如果仅仅第二个条件匹配又会怎样？这条 SQL 最终会是这样:

```xml
SELECT * FROM BLOG
WHERE
AND title like ‘someTitle’
```

这个查询也会失败。这个问题不能简单地用条件句式来解决，如果你也曾经被迫这样写过，那么你很可能从此以后都不会再写出这种语句了。

MyBatis 有一个简单的处理，这在 90% 的情况下都会有用。而在不能使用的地方，你可以自定义处理方式来令其正常工作。一处简单的修改就能达到目的：

```
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

*where* 元素只会在至少有一个子元素的条件返回 SQL 子句的情况下才去插入“WHERE”子句。而且，若语句的开头为“AND”或“OR”，*where* 元素也会将它们去除。

如果 *where* 元素没有按正常套路出牌，我们可以通过自定义 trim 元素来定制 *where* 元素的功能。比如，和 *where* 元素等价的自定义 trim 元素为：

```
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

*prefixOverrides* 属性会忽略通过管道分隔的文本序列（注意此例中的空格也是必要的）。它的作用是移除所有指定在 *prefixOverrides* 属性中的内容，并且插入 *prefix* 属性中指定的内容。

类似的用于动态更新语句的解决方案叫做 *set*。*set* 元素可以用于动态包含需要更新的列，而舍去其它的。比如：

```
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

这里，*set* 元素会动态前置 SET 关键字，同时也会删掉无关的逗号，因为用了条件语句之后很可能就会在生成的 SQL 语句的后面留下这些逗号。（译者注：因为用的是“if”元素，若最后一个“if”没有匹配上而前面的匹配上，SQL 语句的最后就会有一个逗号遗留）

若你对 *set* 元素等价的自定义 trim 元素的代码感兴趣，那这就是它的真面目：

```
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```

注意这里我们删去的是后缀值，同时添加了前缀值。

### foreach

动态 SQL 的另外一个常用的操作需求是对一个集合进行遍历，通常是在构建 IN 条件语句的时候。比如：

```xml
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list"
      open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```

*foreach* 元素的功能非常强大，它允许你指定一个集合，声明可以在元素体内使用的集合项（item）和索引（index）变量。它也允许你指定开头与结尾的字符串以及在迭代结果之间放置分隔符。这个元素是很智能的，因此它不会偶然地附加多余的分隔符。

**注意** 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象传递给 *foreach* 作为集合参数。当使用可迭代对象或者数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。

到此我们已经完成了涉及 XML 配置文件和 XML 映射文件的讨论。下一章将详细探讨 Java API，这样就能提高已创建的映射文件的利用效率。

**特别需要注意的是**

这种使用动态标签进行批量操作的方式存在一定的缺点,比如数据量特别大的时候,拼接出来的 SQL 语句也非常大,MySQL 的服务端对于接受的数据包有大小限制,属性为诶

```
max_allowed_packet=4M
```

需要修改这个配置文件才能解决这个问题

在我们全局配置微件中,可以配置默认的 Executor 的类型,其中有一种 BatchExecutor

```xml
<setting name = "defaultExecutorType" value="BATCH"/>
```

也可以在创建会话的时候指定执行器的类型

```java
SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)
```

实际上就是`BatchExecutor`类底层调用的还是 JDBC 的`ps.addBatch()`方法,也是攒了一批 SQL 后再发送

```java
    /**
     * 原生JDBC的批量操作方式 ps.addBatch()
     *
     * @throws IOException
     */
    @Test
    public void testJdbcBatch() throws IOException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 打开连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/notes", "root", "root");
            ps = conn.prepareStatement(
                    "INSERT into blog values (?, ?, ?)");

            for (int i = 1000; i < 101000; i++) {
                Blog blog = new Blog();
                ps.setInt(1, i);
                ps.setString(2, String.valueOf(i));
                ps.setInt(3, 1001);
                ps.addBatch();
            }
            ps.executeBatch();
//            conn.commit();
            ps.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
```



### script

要在带注解的映射器接口类中使用动态 SQL，可以使用 *script* 元素。比如:

```
    @Update({"<script>",
      "update Author",
      "  <set>",
      "    <if test='username != null'>username=#{username},</if>",
      "    <if test='password != null'>password=#{password},</if>",
      "    <if test='email != null'>email=#{email},</if>",
      "    <if test='bio != null'>bio=#{bio}</if>",
      "  </set>",
      "where id=#{id}",
      "</script>"})
    void updateAuthorValues(Author author);
```

### bind

`bind` 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。比如：

```
<select id="selectBlogsLike" resultType="Blog">
  <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
  SELECT * FROM BLOG
  WHERE title LIKE #{pattern}
</select>
```

### 多数据库支持

一个配置了“_databaseId”变量的 databaseIdProvider 可用于动态代码中，这样就可以根据不同的数据库厂商构建特定的语句。比如下面的例子：

```
<insert id="insert">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    <if test="_databaseId == 'oracle'">
      select seq_users.nextval from dual
    </if>
    <if test="_databaseId == 'db2'">
      select nextval for seq_users from sysibm.sysdummy1"
    </if>
  </selectKey>
  insert into users values (#{id}, #{name})
</insert>
```

### 动态 SQL 中的可插拔脚本语言

MyBatis 从 3.2 开始支持可插拔脚本语言，这允许你插入一种脚本语言驱动，并基于这种语言来编写动态 SQL 查询语句。

可以通过实现以下接口来插入一种语言：

```
public interface LanguageDriver {
  ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
  SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);
  SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);
}
```

一旦设定了自定义语言驱动，你就可以在 mybatis-config.xml 文件中将它设置为默认语言：

```
<typeAliases>
  <typeAlias type="org.sample.MyLanguageDriver" alias="myLanguage"/>
</typeAliases>
<settings>
  <setting name="defaultScriptingLanguage" value="myLanguage"/>
</settings>
```

除了设置默认语言，你也可以针对特殊的语句指定特定语言，可以通过如下的 `lang` 属性来完成：

```
<select id="selectBlog" lang="myLanguage">
  SELECT * FROM BLOG
</select>
```

或者，如果你使用的是映射器接口类，在抽象方法上加上 `@Lang` 注解即可：

```
public interface Mapper {
  @Lang(MyLanguageDriver.class)
  @Select("SELECT * FROM BLOG")
  List<Blog> selectBlog();
}
```

**注意** 可以将 Apache Velocity 作为动态语言来使用，更多细节请参考 MyBatis-Velocity 项目。

你前面看到的所有 xml 标签都是由默认 MyBatis 语言提供的，而它由别名为 `xml` 的语言驱动器 `org.apache.ibatis.scripting.xmltags.XmlLanguageDriver` 所提供。

### 嵌套查询延迟加载

我们在查询业务数据的时候经常会遇到跨表关联查询的情况，比如查询员工就会关 联部门(一对一)，查询成绩就会关联课程(一对一)，查询订单就会关联商品(一对 多)，等等

我们映射结果有两个标签，一个是 resultType，一个是 resultMap。

resultType 是 select 标签的一个属性，适用于返回 JDK 类型(比如 Integer、String 等等)和实体类。这种情况下结果集的列和实体类的属性可以直接映射。如果返回的字段无法直接映射，就要用 resultMap 来建立映射关系。 对于关联查询的这种情况，通常不能用 resultType 来映射。用 resultMap 映射，要么就是修改 dto(Data Transfer Object)，在里面增加字段，这个会导致增加很多无关 的字段。要么就是引用关联的对象，比如 Blog 里面包含了一个 Author 对象，这种情况 下就要用到关联查询(association，或者嵌套查询)，MyBatis 可以帮我们自动做结果 的映射。

```xml
<!-- 根据文章查询作者，一对一查询的结果，嵌套查询 -->
<resultMap id="BlogWithAuthorResultMap" type="com.gupaoedu.domain.associate.BlogAndAuthor">
    <id column="bid" property="bid" jdbcType="INTEGER"/>
    <result column="name" property="name" jdbcType="VARCHAR"/>
    <!-- 联合查询，将author的属性映射到ResultMap -->
    <association property="author" javaType="com.gupaoedu.domain.Author">
        <id column="author_id" property="authorId"/>
        <result column="author_name" property="authorName"/>
    </association>
</resultMap>

<!-- 另一种联合查询(一对一)的实现，但是这种方式有“N+1”的问题 -->
<resultMap id="BlogWithAuthorQueryMap" type="com.gupaoedu.domain.associate.BlogAndAuthor">
    <id column="bid" property="bid" jdbcType="INTEGER"/>
    <result column="name" property="name" jdbcType="VARCHAR"/>
    <association property="author" javaType="com.gupaoedu.domain.Author"
                 column="author_id" select="selectAuthor"/> <!-- selectAuthor 定义在下面-->
</resultMap>
```

我们只执行了一次查询员工信息的 SQL(所谓的 1)，如果返回了 N 条记录，就会 再发送 N 条到数据库查询部门信息(所谓的 N)，这个就是我们所说的 N+1 的问题。 这样会白白地浪费我们的应用和数据库的性能。
如果我们用了嵌套查询的方式，怎么解决这个问题?能不能等到使用部门信息的时 候再去查询?这个就是我们所说的延迟加载，或者叫懒加载。
在 MyBatis 里面可以通过开启延迟加载的开关来解决这个问题。

在 settings 标签里面可以配置:

```xml
    <settings>
        <!-- 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。默认 false  -->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!-- 当开启时，任何方法的调用都会加载该对象的所有属性。默认 false，可通过select标签的 fetchType来覆盖-->
        <setting name="aggressiveLazyLoading" value="false"/>
        <!--  Mybatis 创建具有延迟加载能力的对象所用到的代理工具，默认JAVASSIST -->
        <!--<setting name="proxyFactory" value="CGLIB" />-->
    </settings>

```

- lazyLoadingEnabled决定了是否延迟加载
- aggressiveLazyLoading 决定了是不是对象的锁头方法都会触发查询

**值得注意的是**

- 没有开启延迟加载的开关，会连续发送两次查询;
- 开启了延迟加载的开关，调用 blog.getAuthor()以及默认的(equals,clone,hashCode,toString)时才会发起第二次查询，其他方法并不会触发查询，比如 blog.getName();
- 如果开启了 aggressiveLazyLoading=true，其他方法也会触发查询，比如blog.getName()。

## 问答环节

#### 当开启了延迟加载的开关，对象是怎么变成代理对象的?

DefaultResultSetHandler.createResultObject()

使用的是动态代理对象的方法,MyBatis有哪些实现动态代理的方式?

- JAVASSIST 就是使用 JDK 代理
- CGLIB 使用的是 Cglib 代理

