## 编程方式使用 ObjectProvider 获取依赖

Spring 框架 4.3 以后介绍了一个类`ObjectProvider<T>` 拓展自`ObjectFactory<T>`接口.这个新的接口提供了编程方式获取依赖

## 通过 ObjectProvider 获取实例

我们可以使用`ObjectProvider`注入到任何 Spring管理的 bean 中

```java
@Autowired
private ObjectProvider<ExampleBean> objectProvider;
```

Spring 5.1 添加了一个方法获取`BeanFactory`

```java
ObjectProvider<T> getBeanProvider(Class<T> requiredType);
```

例子

```java
ObjectProvider<ExampleBean> beanProvider = context.getBeanProvider(ExampleBean.class);
```

```java
ObjectProvider<T> getBeanProvider(ResolvableType requiredType);
```

例子

```java
 ResolvableType resolvableType = ResolvableType.forClass(ExampleBean.class);
 ObjectProvider<ExampleBean> beanProvider = context.getBeanProvider(resolvableType);
```

## ObjectProvider 方法

```java
T getIfAvailable()  throws BeansException
```

返回 bean 的实例如果注册到了 SpringContext,不然就返回 null

```java
T getIfUnique()  throws BeansException
```

返回 bean 的实例,如果只有一个 T类型的实例,如果没有,会返回 null,如果多余 3 个,会抛出异常`NoUniqueBeanDefinitionException`

```java
T getObject()  throws BeansException
```

返回 bean 的实例,如果没有找到`NoSuchBeanDefinitionException`,如果多余一个会`NoUniqueBeanDefinitionException`,继承自`ObjectFactory`

```java
T getObject(java.lang.Object... args)  throws BeansException
```

和上一个方法一样,但是支持指定形参