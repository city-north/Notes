# 使用 ObjectProvider 去注入更小作用域 Bean

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

为了解决 [[**更小作用域 Bean 注入问题**]](30-injecting-prototype-bean.md) 

在上个例子( [35-using-jsr-330-provider-to-inject-narrower-scoped-bean.md](35-using-jsr-330-provider-to-inject-narrower-scoped-bean.md) )中我们介绍了`javax.inject.Provider<T>`接口,Spring 给我们提供了一个类似的接口

`ObjectProvider`同样可以解决这个问题



```java
public class MyPrototypeBean {
  private String dateTimeString = LocalDateTime.now().toString();
  private String name;

  public MyPrototypeBean(String name) {
      this.name = name;
  }

  public String getDateTime() {
      return dateTimeString;
  }

  public String getName() {
      return name;
  }
}
```

```java
package com.logicbig.example;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class MySingletonBean {

  @Autowired
  private ObjectProvider<MyPrototypeBean> myPrototypeBeanProvider;

  public void showMessage() {
      MyPrototypeBean bean = myPrototypeBeanProvider.getIfAvailable(
              () -> new MyPrototypeBean("Default Bean"));
      System.out.printf("Time: %s from bean: %s - instance: %s%n", bean.getDateTime(), bean.getName(),
              System.identityHashCode(bean));
  }
}
```

## Java配置以及 main方法

```java
@Configuration
public class AppConfig {

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public MyPrototypeBean prototypeBean() {
      return new MyPrototypeBean("Registered Bean");
  }

  @Bean
  public MySingletonBean singletonBean() {
      return new MySingletonBean();
  }

  public static void main(String[] args) throws InterruptedException {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(AppConfig.class);
      MySingletonBean bean = context.getBean(MySingletonBean.class);
      bean.showMessage();
      Thread.sleep(1000);

      bean = context.getBean(MySingletonBean.class);
      bean.showMessage();
  }
}
```

输出

```
Time: 2018-11-11T14:46:30.550 from bean: Registered Bean - instance: 513700442
Time: 2018-11-11T14:46:31.558 from bean: Registered Bean - instance: 1195067075
```

如果我们不使用 prototype scope,那么两个 bean 都会使用 Singleton 

```java
@Configuration
public class AppConfig {

  @Bean
  //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public MyPrototypeBean prototypeBean() {
      return new MyPrototypeBean("Registered Bean");
  }
 ....
}
```

```
Time: 2018-11-11T14:48:55.219 from bean: Registered Bean - instance: 1669712678
Time: 2018-11-11T14:48:55.219 from bean: Registered Bean - instance: 1669712678
```

如果我们注册 MyprototypeBean,当我们使用`getIfAvailable()`方法的时候,默认的 bean 就会被注册

```java
@Configuration
public class AppConfig {

  //@Bean
  //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public MyPrototypeBean prototypeBean() {
      return new MyPrototypeBean("Registered Bean");
  }
 ....
}
```

```
Time: 2018-11-11T14:51:53.862 from bean: Default Bean - instance: 1906808037
Time: 2018-11-11T14:51:54.868 from bean: Default Bean - instance: 1579526446
```