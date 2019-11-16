# 注入点使用 `@Lazy`

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其中代码仅用于学习笔记,不用于商业用途



始于 Spring 4.3 , `@Lazy`可以在注入点使用,意味着

- `@Lazy` 和`@Autowired`还有`@Inject`和`@Resource`一起使用
- 延迟目标 bean 的初始化,直到注入点类使用到这个 bean才会被初始化,即使注入点类已经被初始化

总结来说:

- **@Lazy with @Bean (or @Lazy with @Component):** 在程序启动时不会急切地加载,知道它使用的时候

- **@Lazy with @Autowired :** 不会在类初始化的时候加载和这个依赖,知道它第一次被调用

## 例子

- MyEagerBean 使用的默认在 spring启动时初始化
- MyLazyBean 使用懒加载

```java
public class MyConfig {

  @Bean
  public MyEagerBean eagerBean () {
      return new MyEagerBean();
  }

  @Bean
  @Lazy
  public MyLazyBean lazyBean () {
      return new MyLazyBean();
  }
}
```

下面的类`MyEagerBean` 使用了`@Autowired`注解(注入点).这个 bean 会立马加载,使用`@Lazy`后会懒加载

```java
public class MyEagerBean {

  @Autowired
  @Lazy
  private MyLazyBean myLazyBean;

  @PostConstruct
  public void init () {
      System.out.println(getClass().getSimpleName() + " has been initialized");
  }

  public void doSomethingWithLazyBean () {
      System.out.println("Using lazy bean");
      myLazyBean.doSomething();
  }
}
```

测试类

```java
public class LazyExampleMain {

  public static void main (String[] args) {
      ApplicationContext context =
                new AnnotationConfigApplicationContext(
                          MyConfig.class);
      System.out.println("--- container initialized ---");
      MyEagerBean bean = context.getBean(MyEagerBean.class);
      System.out.println("MyEagerBean retrieved from bean factory");
      bean.doSomethingWithLazyBean();
  }
}
```

输出

```java
MyEagerBean has been initialized
--- container initialized ---
MyEagerBean retrieved from bean factory
Using lazy bean
MyLazyBean has been initialized
inside lazy bean doSomething()
```

如果我们拿掉`@Lazy`

```java
public class MyEagerBean {

  @Autowired
  //@Lazy
  private MyLazyBean myLazyBean;
  .......
}
```

输出

```java
MyLazyBean has been initialized
MyEagerBean has been initialized
--- container initialized ---
MyEagerBean retrieved from bean factory
Using lazy bean
inside lazy bean doSomething()
```

当MyEagerBean被加载的时候,Spring 容器会初始化它的依赖,如果有一个`@Lazy`注解标注在注入点,时就不会初始化

## 原理

Soring创建和使用了一个 lazy-resolution 代理类到注入点(标注有`@Autowired` and `@Lazy`),而不是直接进行初始化