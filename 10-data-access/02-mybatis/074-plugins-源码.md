# plugins源码

[TOC]

## 注册阶段

我们使用 SqlSessionFactoryBuilder 构建一个 SqlSessionFactory

```java
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

SqlSessionFactoryBuilder 委托给 XMLConfigBuilder

```java
  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
      return build(parser.parse());
		....//省略
  }
```

XMLConfigBuilder 的 parse 方法就是 解析 configuration 标签的地方

```java
public Configuration parse() {
  if (parsed) {
    throw new BuilderException("Each XMLConfigBuilder can only be used once.");
  }
  parsed = true;
  parseConfiguration(parser.evalNode("/configuration"));
  return configuration;
}
```

XMLConfigBuilder 中的 parseConfiguration 主要解析了所有配置文件,我们可以看到在

```java
//org.apache.ibatis.builder.xml.XMLConfigBuilder#parseConfiguration  
private void parseConfiguration(XNode root) {
    try {
      //issue #117 read properties first
      propertiesElement(root.evalNode("properties"));
 			///....省略其他解析
      pluginElement(root.evalNode("plugins")); // 解析插件
      ///....省略
```

具体解析插件

```java
//org.apache.ibatis.builder.xml.XMLConfigBuilder#pluginElement
  private void pluginElement(XNode parent) throws Exception {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        //拿到插件注册的类的全限定名
        String interceptor = child.getStringAttribute("interceptor");
        // 拿到子节点
        Properties properties = child.getChildrenAsProperties();
        //解析类,拿到 插件的实体类
        Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).getDeclaredConstructor().newInstance();
        // 设置属性
        interceptorInstance.setProperties(properties);
        // 将插件放入 configuration
        configuration.addInterceptor(interceptorInstance);
      }
    }
  }
```

至此,我们已经把拦截器的实体类放入了 configuration 类中

## 代理四大对象

我们使用 sqlSessionFactory 创建一个 SqlSessions , 

```java
SqlSession session = sqlSessionFactory.openSession();
```

configuration.newExecutor 实际上是创建了一个执行器, 在里面进行了代理

```java
//org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSessionFromDataSource
  private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
    Transaction tx = null;
      final Environment environment = configuration.getEnvironment();
      final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
      tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
    // 创建一个新的执行器
      final Executor executor = configuration.newExecutor(tx, execType);
      return new DefaultSqlSession(configuration, executor, autoCommit);
		//...省略代码
    }
  }

```

这里直接调用了 [注册阶段](#注册阶段) 在 Configuration 里面注入的拦截器链路,创建代理

```java
//org.apache.ibatis.session.Configuration#newExecutor(org.apache.ibatis.transaction.Transaction, org.apache.ibatis.session.ExecutorType)
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    if (ExecutorType.BATCH == executorType) {
      executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
      executor = new ReuseExecutor(this, transaction);
    } else {
      executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
      executor = new CachingExecutor(executor);
    }
  // 使用连接器链来带代理
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
  }
```

代理链路层层创建代理, 代理也可以被代理

```java
public class InterceptorChain {

  private final List<Interceptor> interceptors = new ArrayList<>();


  public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
      //直接使用拦截器来进行代理
      target = interceptor.plugin(target);
    }
    return target;
  }

  public void addInterceptor(Interceptor interceptor) {
    interceptors.add(interceptor);
  }

  public List<Interceptor> getInterceptors() {
    return Collections.unmodifiableList(interceptors);
  }

}

//Interceptor 默认的代理方式
  default Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }
```

Interceptor 默认的代理方式 是使用 Plugin 类的 wrap方法代理

```java
//org.apache.ibatis.plugin.Plugin#wrap  
public static Object wrap(Object target, Interceptor interceptor) {
  //获取到拦截器的签名
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
    Class<?> type = target.getClass();
    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
    if (interfaces.length > 0) {
      // JDK 代理
      return Proxy.newProxyInstance(
          type.getClassLoader(),
          interfaces,
          new Plugin(target, interceptor, signatureMap));
    }
    return target;
  }
```

这样就完成了层层代理

## 执行拦截器

执行拦截器实际上是在执行代理对象的方法的时候执行的, 前面两个步骤将

```java
//org.apache.ibatis.session.defaults.DefaultSqlSession#selectList(String,Object,RowBounds);
@Override
public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
  try {
    MappedStatement ms = configuration.getMappedStatement(statement);
    // 实际上这里的 executor 就是 插件代理的类了 ,执行 query 就会走 org.apache.ibatis.plugin.Plugin#invoke 方法
    return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
  } catch (Exception e) {
    throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
  } finally {
    ErrorContext.instance().reset();
  }
}
```

```java
//org.apache.ibatis.plugin.Plugin#invoke 
@Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
      if (methods != null && methods.contains(method)) {
        // 执行拦截的方法
        return interceptor.intercept(new Invocation(target, method, args));
      }
      return method.invoke(target, args);
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }
```

然后就执行到了com.github.pagehelper.PageInterceptor#intercept 具体的拦截方法

