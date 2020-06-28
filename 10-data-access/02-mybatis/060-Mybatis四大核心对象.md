# MyBatis四大核心对象

| 对象                                                | 生命周期                   |
| --------------------------------------------------- | -------------------------- |
| [SqlSessionFactoryBuiler](#SqlSessionFactoryBuiler) | 方法局部                   |
| [SqlSessionFactory](#SqlSessionFactory)             | 应用级别(application)      |
| [SqlSession](#SqlSession)                           | 请求和操作(request/method) |
| [Mapper](#Mapper)                                   | 方法(method)               |

## SqlSessionFactoryBuiler

>  [061-SqlSessionFactoryBuilder.md](061-SqlSessionFactoryBuilder.md) 

`SqlSessionFactoryBuiler`用来构建 `SqlSessionFactory` 的，而 `SqlSessionFactory `只需要一个，所以只要构建了这一个 SqlSessionFactory，它的使命就完成了，也就没有存在的意义了。所以它的生命周期只存在于**方法的局部**

## SqlSessionFactory

>  [062-SqlSessionFactory.md](062-SqlSessionFactory.md) 

`SqlSessionFactory` 是用来创建` SqlSession` 的，每次应用程序访问数据库，都需要创建一个会话。因为我们一直有创建会话的需要,所以 `SqlSessionFactory` 应该存在于 应用的**整个生命周期中(作用域是应用作用域)**。创建 `SqlSession` 只需要一个实例来做 这件事就行了，否则会产生很多的混乱，和浪费资源。所以我们要采用**单例模式。**

## SqlSession

>  [063-sqlSession.md](063-sqlSession.md) 

SqlSession 是一个会话，因为它不是线程安全的，不能在线程间共享。所以我们在请求开始的时候创建一个 SqlSession 对象，在请求结束或者说方法执行完毕的时候要及 时关闭它(一次请求或者操作中)。

## Mapper

>  [064-mapper.md](064-mapper.md) 

Mapper(实际上是一个代理对象)是从 SqlSession 中获取的。

```java
BlogMapper mapper = session.getMapper(BlogMapper.class);
```

它的作用是发送 SQL 来操作数据库的数据。它应该在一个 `SqlSession` 事务方法之内。

