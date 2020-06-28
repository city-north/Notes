# 通用 Mapper

问题:当我们的表字段发生变化的时候，我们需要修改实体类和 Mapper 文件定义 的字段和方法。如果是增量维护，那么一个个文件去修改。如果是全量替换，我们还要 去对比用 MBG 生成的文件。字段变动一次就要修改一次，维护起来非常麻烦。

**解决这个问题，我们有两种思路。**

第一个，因为 MyBatis 的 Mapper 是支持继承的,所 以 我 们 可 以 把 我 们 的 Mapper.xml 和 Mapper 接口都分成两个文件。一个是 MBG 生成的，这部分是固定不变 的。然后创建 DAO 类继承生成的接口，变化的部分就在 DAO 里面维护。

```
public interface BlogMapperExt extends BlogMapper {
    /**
     * 根据名称查询文章
     * @param name
     * @return
     */
    public Blog selectBlogByName(String name);
}
```

```xml
<mapper namespace="com.gupaoedu.mapper.BlogMapperExt">
    <!-- 只能继承statement，不能继承sql、resultMap等标签 -->
    <resultMap id="BaseResultMap" type="blog">
        <id column="bid" property="bid" jdbcType="INTEGER"/>
        <result column="name" property="name" jdbcType="VARCHAR"/>
        <result column="author_id" property="authorId" jdbcType="INTEGER"/>
    </resultMap>

    <!-- 在parent xml 和child xml 的 statement id相同的情况下，会使用child xml 的statement id -->
    <select id="selectBlogByName" resultMap="BaseResultMap" statementType="PREPARED">
        select * from blog where name = #{name}
    </select>
</mapper>
```

##### 思考:既然针对每张表生成的基本方法都是一样的，也就是公共的方法部分代码都 是一样的，我们能不能把这部分合并成一个文件，让它支持泛型呢?

编写一个支持泛型的通用接口，比如叫 `GPBaseMapper<T>`，把实体类作为参数传 入。

这个接口里面定义了大量的增删改查的基础方法，这些方法都是支持泛型的。

自定义的 Mapper 接口继承该通用接口，例如 `BlogMapper extends GPBaseMapper<Blog>`，自动获得对实体类的操作方法。遇到没有的方法，我们依然 可以在我们自己的 Mapper 里面编写。

