# 插件（plugins）

## 是什么

插件是 Mybatis 的一个强大的机制,Mybatis 预留了插件的接口,让 Mybatis 更加容易拓展

## 核心类

- Interceptor接口
- 自定义拦截器(实现类)
- Plugin 工具类用来创建代理对象
- Invocation 内的 wrap 方法进行包装,proceed方法执行被拦截方法
- InterceptorChain 插件的链

MyBatis 允许你在已映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：

- Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
- ParameterHandler (getParameterObject, setParameters)
- ResultSetHandler (handleResultSets, handleOutputParameters)
- StatementHandler (prepare, parameterize, batch, update, query)

## 拦截器接口的介绍

Mybatis 插件可以用来实现拦截器接口 Interceptor

![image-20200220224047539](assets/image-20200220224047539.png)

- `Intercept`Mybatis 运行时要执行的拦截方法,通过该方法的参数 Invocation 可以得到很多有用的信息

- `plugin` 这个方法参数 target 即使要拦截器拦截的对象,这个方法会在创建被拦截的接口实现类时被调用,这个方法非常简单,只需要调用 Mybatis 实现的`Plugin(org.apache.ibatis.plugin.plgin)`类的 wrap 静态方法就可以通过 Java 的 JDK 动态代理拦截目标对象

```
    @Override
    public Object plugin(Object target) {
    //自动判断拦截器的签名和被拦截对象的接口是否匹配,只有匹配的情况下才会使用动态代理拦截目标对象,因此不必要做额外的逻辑判断
      return Plugin.wrap(target, this);
    }
```

- setProperties 用来传递参数改变插件的行为,通常是读取配置文件中 plugins 标签下的属性

## 拦截器签名介绍

```
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class PageInterceptor implements Interceptor {

```

上面是一个拦截器典型的签名 PageHelper 相关的拦截器,

@Signature 注解包含以下三个属性

- type: 设置拦截的接口,可选值是前面提到的 4 个接口
- method : 设置拦截接口中的方法名,可选值是前面 4 个接口对应的方法,需要和接口匹配
- Args : 设置拦截方法的参数类型数组,通过方法名和参数类型可以确定为一个方法

## Question

- 不修改原有的代码,怎么改变和增强对象的行为

动态代理模式

- 插件的拦截链路怎么形成?如何做到层层的拦截

责任连模式

- 有哪些对象允许被代理,哪些方法被拦截
- 四大天王什么时候被代理的,代理对象是什么时候创建的

使用 JDK 的动态代理来实现的,executor 是在 openSession 的时候创建的代理对象,其他三个一旦被创建就会调用 InterceptorChan 来之执行拦截器

- 多个插件的情况下,代理能不能被代理,代理顺序和调用顺序的关系

- 谁来创建代理对象

- 什么时候创建代理对象,启动,创建会话和执行 sql?

- 被代理以后,调用的是设么方法,怎么调用到原被代理对象的方法

  四大天王被代理以后,调用的是 `intercept` 方法, 和 plugin 方法,创建代理类

  使用 

### Answer

- 

这些类中方法的细节可以通过查看每个方法的签名来发现，或者直接查看 MyBatis 发行包中的源代码。 如果你想做的不仅仅是监控方法的调用，那么你最好相当了解要重写的方法的行为。 因为如果在试图修改或重写已有方法的行为的时候，你很可能在破坏 MyBatis 的核心模块。 这些都是更低层的类和方法，所以使用插件的时候要特别当心。

通过 MyBatis 提供的强大机制，使用插件是非常简单的，只需实现 Interceptor 接口，并指定想要拦截的方法签名即可。

```
// ExamplePlugin.java
@Intercepts({@Signature(
  type= Executor.class,
  method = "update",
  args = {MappedStatement.class,Object.class})})
public class ExamplePlugin implements Interceptor {
  private Properties properties = new Properties();
  public Object intercept(Invocation invocation) throws Throwable {
    // implement pre processing if need
    Object returnObject = invocation.proceed();
    // implement post processing if need
    return returnObject;
  }
  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}
<!-- mybatis-config.xml -->
<plugins>
  <plugin interceptor="org.mybatis.example.ExamplePlugin">
    <property name="someProperty" value="100"/>
  </plugin>
</plugins>
```

上面的插件将会拦截在 Executor 实例中所有的 “update” 方法调用， 这里的 Executor 是负责执行低层映射语句的内部对象。

**提示** **覆盖配置类**

除了用插件来修改 MyBatis 核心行为之外，还可以通过完全覆盖配置类来达到目的。只需继承后覆盖其中的每个方法，再把它传递到 SqlSessionFactoryBuilder.build(myConfig) 方法即可。再次重申，这可能会严重影响 MyBatis 的行为，务请慎之又慎。



- 