# Spring中的 Stream 支持 (ObjectProvider)

这个方法返回一个所有bean 实例的匹配的序列流.没有顺序(基于注册顺序)

```java
default java.util.stream.Stream<T> stream()
```

根据`@Order`和`Ordered`接口排序后的:

```java
default java.util.stream.Stream<T> orderedStream()
```

## 代码

#### stream() example

```java
@Configuration
public class StreamExample {

  @Bean
  MsgBean msgBean() {
      return new MsgBean("test msg 1");
  }

  @Bean
  MsgBean msgBean2() {
      return new MsgBean("test msg 2");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(StreamExample.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      beanProvider.iterator().forEachRemaining(System.out::println);
  }
}
```

#### orderedStream() examples

```java
@Configuration
public class OrderedStreamExample {

  @Bean
  @Order(2)
  MsgBean msgBean() {
      return new MsgBean("test msg 1");
  }

  @Bean
  @Order(1)
  MsgBean msgBean2() {
      return new MsgBean("test msg 2");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(OrderedStreamExample.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      System.out.println("-- default order --");
      beanProvider.stream().forEach(System.out::println);
      System.out.println("-- ordered by @Order --");
      beanProvider.orderedStream().forEach(System.out::println);
  }
}
```

输出

```
-- default order --
MsgBean{msg='test msg 1'}
MsgBean{msg='test msg 2'}
-- ordered by @Order --
MsgBean{msg='test msg 2'}
MsgBean{msg='test msg 1'}
```

#### 迭代器方法

```java
@Configuration
public class IteratorExample {

  @Bean
  MsgBean msgBean() {
      return new MsgBean("test msg 1");
  }

  @Bean
  MsgBean msgBean2() {
      return new MsgBean("test msg 2");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(IteratorExample.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      beanProvider.forEach(System.out::println);
  }
}
```

```
MsgBean{msg='test msg 1'}
MsgBean{msg='test msg 2'}
```

```java
@Configuration
public class IteratorExample2 {

  @Bean
  MsgBean msgBean() {
      return new MsgBean("test msg 1");
  }

  @Bean
  MsgBean msgBean2() {
      return new MsgBean("test msg 2");
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(IteratorExample2.class);
      ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
      beanProvider.iterator().forEachRemaining(System.out::println);
  }
}
```

```java
MsgBean{msg='test msg 1'}
MsgBean{msg='test msg 2'}
```