# 生命周期初始化以及销毁回调(Callback)

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其中代码仅用于学习笔记,不用于商业用途



Spring 提供很多机制来获取声明周期回调,这些回调在你需要在声明周期的特定阶段进行特定的行为时,非常有用.一个典型的应用场景是一个 bean 完全初始化且属性都设置完成后

## 使用`@PostConstruct`和`@PreDestory`

推荐的方式是使用

- `@PostConstruct` : 属性完全被设置完成后
- `@PreDestory`: 销毁前调用方法

```java
public class MyBean {
  private OtherBean otherBean;

  public MyBean() {
      System.out.println("MyBean constructor: " + this);
  }

  @PostConstruct
  public void myPostConstruct() {
      System.out.println("myPostConstruct()");
  }

  @Autowired
  public void setOtherBean(OtherBean otherBean) {
      System.out.println("setOtherBean(): " + otherBean);
      this.otherBean = otherBean;
  }

  public void doSomething() {
      System.out.println("doSomething()");
  }

  @PreDestroy
  public void cleanUp() {
      System.out.println("cleanUp method");
  }
}
```

```java
public class MyBean2 {
  private OtherBean otherBean;

  public MyBean2() {
      System.out.println("MyBean2 constructor: " + this);
  }

  public void myPostConstruct() {
      System.out.println("myPostConstruct()");
  }

  @Autowired
  public void setOtherBean(OtherBean otherBean) {
      System.out.println("setOtherBean(): " + otherBean);
      this.otherBean = otherBean;
  }

  public void doSomething() {
      System.out.println("doSomething()");
  }

  public void cleanUp() {
      System.out.println("cleanUp method");
  }
}
```

```java
@Configuration
public class LifeCycleExample {

  @Bean
  public MyBean myBean() {
      return new MyBean();
  }

  @Bean
  public OtherBean otherBean() {
      return new OtherBean();
  }

  public static void main(String[] args) {
      ConfigurableApplicationContext context =
              new AnnotationConfigApplicationContext(LifeCycleExample.class);

      context.registerShutdownHook();

      System.out.println("-- accessing bean --");
      MyBean bean = context.getBean(MyBean.class);
      bean.doSomething();

      System.out.println("-- finished --");
  }
}
```

#### output

```
MyBean constructor: com.logicbig.example.MyBean@fde068a
setOtherBean(): com.logicbig.example.OtherBean@39b62948
myPostConstruct()
-- accessing bean --
doSomething()
-- finished --
cleanUp method
```

## 理解`registerShutdownHook()`方法

在上面的例子中,我们使用`ConfigurableApplicationContext.registerShutdownHook()`这个方法注册了一个 虚拟机的 shutdown hook ,当 JVM 关闭时,这个 hook 会接收到这个消息,这时，它关闭了底层上下文并且调用`@PreDestory`和其他标准的注册销毁方法.如果你不想使用这个方法,我们就得调用`ConfigurableApplicationContext.close()`方法去关闭 JVM,不然的话,destory 方法不会被调用

在上面的例子,我们使用的是 setter 注入,下面示例展示了使用属性注入以及构造器注入的方式:

这些属性会在调用`@PostContract`方法时被完全初始化

## 使用`@Bean`注解的`initMethod`和`destroyMethod`属性

```java
public class MyBean2 {
  private OtherBean otherBean;

  public MyBean2() {
      System.out.println("MyBean2 constructor: " + this);
  }

  public void myPostConstruct() {
      System.out.println("myPostConstruct()");
  }
    .............
  public void cleanUp() {
      System.out.println("cleanUp method");
  }
}
```



```java
@Configuration
public class LifeCycleExample2 {

  @Bean(initMethod = "myPostConstruct", destroyMethod = "cleanUp")
  public MyBean2 myBean2() {
      return new MyBean2();
  }
    .............
}
```

#### output

```
MyBean2 constructor: com.logicbig.example.MyBean2@614592c9
setOtherBean(): com.logicbig.example.OtherBean@5f99afcd
myPostConstruct()
-- accessing bean --
doSomething()
-- finished --
cleanUp method
```

## 实现接口`InitializingBean`和`DisposableBean`

- 接口`InitializingBean`有一个方法`afterPropertiesSet`,Spring框架会在设置 bean properties 
- 接口`DisposableBean`有一个方法`destroy`,当 JVM发送一个关闭信号时会被 Spring 框架调用

```java
public class MyBean3 implements InitializingBean, DisposableBean {
  private OtherBean otherBean;

  public MyBean3() {
      System.out.println("MyBean3 constructor: " + this);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
      System.out.println("afterPropertiesSet()");
  }
    .............
  @Override
  public void destroy() throws Exception {
      System.out.println("destroy() method");
  }
}
```



```java
@Configuration
public class LifeCycleExample3 {

  @Bean
  public MyBean3 myBean3() {
      return new MyBean3();
  }
    .............
}
```

输出

```java
MyBean3 constructor: com.logicbig.example.MyBean3@1d4834fb
setOtherBean(): com.logicbig.example.OtherBean@130b5770
afterPropertiesSet()
-- accessing bean --
doSomething() :
-- finished --
destroy() method
```

