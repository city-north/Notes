# 对比 hibernate

在使用 Hibernate 的时候，我们需要为实体类建立一些 hbm 的 xml 映射文件(或者类似于`@Table` 的这样的注 解)。例如:

```xml
<hibernate-mapping>
<class name="cn.gupaoedu.vo.User" table="user">
       <id name="id">
           <generator class="native"/>
</id>
<property name="password"/> <property name="cellphone"/>
  <property name="username"/> </class>
</hibernate-mapping>
```

然后通过 Hibernate 提供(session)的增删改查的方法来操作对象。

```java
//创建对象
User user = new User(); user.setPassword("123456"); user.setCellphone("18166669999"); user.setUsername("qingshan");
//获取加载配置管理类
Configuration configuration = new Configuration(); //不给参数就默认加载 hibernate.cfg.xml 文件， configuration.configure();
//创建 Session 工厂对象
SessionFactory factory = configuration.buildSessionFactory(); //得到 Session 对象
Session session = factory.openSession();
//使用 Hibernate 操作数据库，都要开启事务,得到事务对象
Transaction transaction = session.getTransaction();
//开启事务
transaction.begin();
//把对象添加到数据库中
session.save(user);
//提交事务 transaction.commit();
//关闭 Session session.close();
```

我们操作对象就跟操作数据库的数据一样。Hibernate 的框架会自动帮我们生成 SQL 语句(可以屏蔽数据库的差异)，自动进行映射。这样我们的代码变得简洁了，程序的 可读性也提高了。

1. 比如使用 get()、save() 、update()对象的这种方式，实际操作的是所有字段，没有办法指定部分字段，换句话说就是不够灵活。
2. 这种自动生成 SQL 的方式，如果我们要去做一些优化的话，是非常困难的，也就是说可能会出现性能比较差的问题。
3. 不支持动态 SQL(比如分表中的表名变化，以及条件、参数)