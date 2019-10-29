# ApplicationContext 和资源路径的关系

本章主要介绍如果通过 resources 创建 应用环境,包括xml 方式的捷径以及如何使用通配符

## 构造 应用环境

如果形参中没有前缀,Spring 会自动查询核实的`Resource`类型.下面是一些例子

- 创建一个`ClassPathXmlApplicationContext`

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/appContext.xml");
```

- 创建一个`FileSystemXmlApplicationContext`

```java
ApplicationContext ctx =
    new FileSystemXmlApplicationContext("classpath:conf/appContext.xml");
```

- 指定的 classpath 前缀或者标准 URL浅灰会覆盖掉默认的 Resource 类型定义,以前缀为准

```java
ApplicationContext ctx =
    new FileSystemXmlApplicationContext("classpath:conf/appContext.xml");
```

