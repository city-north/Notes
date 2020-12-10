# ShardingJDBC

[TOC]

## 简介

Sharding JDBC是一款轻量级的Java框架，诞生于当当网，主要解决数据在分库分表后的操作问题，是一套完整的解决方案。

在实际使用中，Sharding JDBC操作十分简便，易于理解。我们只需要在Maven中直接添加依赖即可，无须额外部署服务，并且与主流的 ORM 开源框架兼容，便于旧代码的迁移。如果我们的项目使用的是Spring，那么只需要简单修改Spring的配置文件即可。

在功能实现上，Sharding JDBC不但完整地实现了分库分表，还支持读写分离及生成分布式ID的功能。在Sharding JDBC中对事务的支持也十分完善，基本覆盖了实际开发中主流的使用场景。支持的功能如下。

# 分库分表

在分库分表后，我们最关心的就是原有的SQL语句能否正常执行，例如查询、插入、分组、排序等操作。不过不用担心，Sharding JDBC已帮我们全部实现了。

在功能上，Sharding JDBC支持聚合、分组、排序、LIMIT、TOP、=、BETWEEN、IN等查询操作，此外还对级联、笛卡尔积、内外连接查询有良好的支持。

Sharding JDBC在查询方面的优异表现，离不开分片策略的支持。

在Sharding JDBC中主要提供了三种分片策略（分片策略指我们执行的 SQL 最终定位到哪个库及哪张表里）

- StandardShardingStrategy

  > 标准分片策略，只支持单分片键，提供对SQL语句中的=、IN、BETWEEN、AND的分片操作支持。

- ComplexShardingStrategy

  > 复合分片策略，支持多分片键，同样提供对SQL语句中的=、IN、BETWEEN、AND的分片操作支持。

- InlineShardingStrategy

  > Inline表达式分片策略，使用Groovy的Inline表达式，提供对SQL语句中的=和IN的分片操作支持。例如：t_user_${user_id%8}表示t_user表根据user_id按8取模分成8个表，表名称依次为t_user_0、t_user_1……t_user_7。

值得一提的是，由于不同的应用有不同的业务逻辑，所以分片策略的实现也不完全相同，与业务紧密相关，所以在Sharding JDBC框架中并未提供默认的分片策略，而是定义了三种分片策略的规范，也就是我们所说的接口，将最终的实现交给应用开发者来实施。

## 数据分片

- 分库&分表
- 读写分离

> https://shardingsphere.apache.org/document/current/cn/features/read-write-split/

- 分片策略定制化
- 无中心化分布式主键(包括 UUID、雪花、LEAF)

> https://shardingsphere.apache.org/document/current/cn/features/sharding/other-features/key-generator/

## 分布式事务

https://shardingsphere.apache.org/document/current/cn/features/transaction/

- 标准化事务接口 
- XA 强一致事务 
- 柔性事务

## 分布式主键

在没有进行分库分表时，主键自动生成是我们应用的基本需求，在实际开发中我们可以采用主键自增、列生成序列的方式来实现全局唯一主键。

但是在分库分表后，被分割的表存在于不同的库中，或者存在于同一库中。此时，若想继续使用主键生成或列生成序列的方式便行不通，主键会出现重复，不再唯一，在业务上肯定行不通，这时就需要引入外部功能来解决唯一性的问题。
对于全局唯一性的问题，目前有许多第三方解决方案，比如：依靠特定算法生成不重复键UUID等，或者通过引入全局ID生成服务，或者通过时间戳等形式解决。

在Sharding JDBC中也提供了一种主键生成机制，集中采用snowflake算法实现，生成的数据为64bit的整型数据，以保证数据的全局唯一性。

## 兼容性

对于一个应用来说，在系统设计之初并不会考虑分库分表的问题，而是在系统发展到一定的业务量之后，数据量过大导致数据库性能下降，此时就不得不引入分库分表来解决性能问题。但是，此时也有一个棘手的问题，就是引入分库分表后与原有系统该如何兼容。

在兼容性上，对于Sharding JDBC来说可以完全不用操心，Sharding JDBC可与任意ORM框架兼容，例如：JPA、Hibernate、Mybatis、Spring JDBC Template或直接使用JDBC，并且支持任意第三方数据库连接池，例如DBCP、C3P0、BoneCP、Druid等。
现阶段，绝大多数应用都以 Spring+SpringMVC+MyBatis（SSM）的形式存在，下面我们通过在SSM中与Sharding JDBC集成来演示具体操作。

