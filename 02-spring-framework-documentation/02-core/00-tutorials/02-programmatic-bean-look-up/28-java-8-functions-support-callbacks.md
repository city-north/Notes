# ObjectProvider 支持 Java 8函数式编程 

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

Spring 5.0 以后,`ObjectProvider` 接口支持`java.util.function`回调

```java
default T getIfAvailable(java.util.function.Supplier<T> defaultSupplier) throws BeansException

```

如果存在,返回,如果不存在,会执行传入函数并返回

```java
default void ifAvailable(java.util.function.Consumer<T> dependencyConsumer)  throws BeansException
```

如果不存在,会执行 传入的 Consumer

```java
default T getIfUnique(java.util.function.Supplier<T> defaultSupplier)  throws BeansException
```

如果是唯一,返回,不唯一执行传入函数并返回

```
default void ifUnique(java.util.function.Consumer<T> dependencyConsumer)  throws BeansException
```

如果不存在,会执行传入的Consumer

## 实例

```java
public class MsgBean {
  private String msg;

  public MsgBean(String msg) {
      this.msg = msg;
  }

  public void showMessage() {
      System.out.println("msg: " + msg);
  }
}
```

#### getIfAvailable(defaultSupplier) example

```java
@Configuration
public class GetIfAvailableWithSupplier {
  //uncomment @Bean then getIfAvailable will return this instance
  //@Bean
  MsgBean msgBean() {
      return new MsgBean("test msg");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(GetIfAvailableWithSupplier.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      MsgBean exampleBean = beanProvider.getIfAvailable(() -> new MsgBean("default msg"));
      exampleBean.showMessage();
  }
}
```

```
msg: default msg
```

#### ifAvailable(defaultConsumer) example

```java
@Configuration
public class IfAvailableWithConsumer {
  //remove @Bean annotation then getIfAvailable will print nothing
  @Bean
  MsgBean msgBean() {
      return new MsgBean("test msg");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(IfAvailableWithConsumer.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      beanProvider.ifAvailable(msgBean -> msgBean.showMessage());
  }
}
```

```JAVA
msg: test msg
```

#### getIfUnique(defaultSupplier) example

```java
@Configuration
public class GetIfUniqueWithSupplier {
  @Bean
  MsgBean msgBean() {
      return new MsgBean("test msg 1");
  }

  //if we remove @Bean here, then 'test msg 1' will print
  @Bean
  MsgBean msgBean2() {
      return new MsgBean("test msg 2");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(GetIfUniqueWithSupplier.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      MsgBean exampleBean = beanProvider.getIfUnique(() -> new MsgBean("default msg"));
      exampleBean.showMessage();
  }
}
```

```java
msg: default msg
```

#### ifUnique(dependencyConsumer) example

```java
@Configuration
public class IfUniqueWithConsumer {
  @Bean
  MsgBean msgBean() {
      return new MsgBean("test msg 1");
  }

  //if uncomment @Bean annotation here, then nothing will print
  //@Bean
  MsgBean msgBean2() {
      return new MsgBean("test msg 2");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(IfUniqueWithConsumer.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      beanProvider.ifUnique(msgBean -> msgBean.showMessage());
  }
}
```

```
msg: test msg 1
```

