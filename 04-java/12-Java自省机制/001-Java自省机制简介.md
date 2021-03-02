# Java自省机制简介

https://www.cs.auckland.ac.nz/references/java/java1.5/tutorial/javabeans/introspection/index.html

https://xiaomi-info.github.io/2020/03/16/java-beans-introspection/

[TOC]

## 什么是Java自省

> *Introspection* is the automatic process of analyzing a bean's design patterns to reveal the bean's properties, events, and methods. This process controls the publishing and discovery of bean operations and properties

自省是自动分析一个bean的流程的设计模式, 用来发现 bean 的属性, 事件, 方法 , 这个过程控制了 publish 和 decovery 一个bean的操作和属性

在计算机科学中，内省是指计算机程序在运行时（Run time）检查对象（Object）类型的一种能力，通常也可以称作运行时类型检查。

#### Java官方对Java Beans自省的定义：

> At runtime and in the builder environment we need to be able to figure out which properties, events, and methods a Java Bean supports. We call this process introspection.

从 Java Bean 的角度来看，这里的对象就是 Bean 对象，主要关注点是属性、方法和事件等，也就是说在运行时可以获取相应的信息进行一些处理，这就是 Java Beans 的内省机制。

#### Java自省与反射的区别

Java Beans 内省其实就是对反射的一种封装，这个从源码中或者官方文档中都能看到：

> By default we will use a low level reflection mechanism to study the methods supported by a target bean and then apply simple design patterns to deduce from those methods what properties, events, and public methods are supported.

## JavaBean自省机制详解

### 核心类库

Java Beans 内省机制的核心类是 `Introspector`：

```
* The Introspector class provides a standard way for tools to learn about
* the properties, events, and methods supported by a target Java Bean.
```

操作范围主要包括但不局限于 Java Beans 的属性，事件和方法，具体是基于以下几个类实现：

- BeanInfo
  - Java Bean 信息类
- PropertyDescriptor
  - 属性描述类
- MethodDescriptor
  - 方法描述类
- EventSetDescriptor
  - 事件描述集合

先看一个示例：

定义一个 Java Bean：

```java
public class User {

    private String username;

    private Integer age;

    // getter/setter
    // toString

}
```

测试代码：

```java
@Test
public void test1() throws IntrospectionException {
    //获取 User Bean 信息
    BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class);
    //属性描述
    PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();
    System.out.println("属性描述：");
    Stream.of(propertyDescriptors).forEach(System.out::println);
    //方法描述
    System.out.println("方法描述：");
    MethodDescriptor[] methodDescriptors = userBeanInfo.getMethodDescriptors();
    Stream.of(methodDescriptors).forEach(System.out::println);
    //事件描述
    System.out.println("事件描述：");
    EventSetDescriptor[] eventSetDescriptors = userBeanInfo.getEventSetDescriptors();
    Stream.of(eventSetDescriptors).forEach(System.out::println);
}
```

输出结果

```java
属性描述：
java.beans.PropertyDescriptor[name=age; propertyType=class java.lang.Integer; readMethod=public java.lang.Integer introspector.bean.User.getAge(); writeMethod=public void introspector.bean.User.setAge(java.lang.Integer)]
java.beans.PropertyDescriptor[name=class; propertyType=class java.lang.Class; readMethod=public final native java.lang.Class java.lang.Object.getClass()]
java.beans.PropertyDescriptor[name=username; propertyType=class java.lang.String; readMethod=public java.lang.String introspector.bean.User.getUsername(); writeMethod=public void introspector.bean.User.setUsername(java.lang.String)]
  
方法描述：
java.beans.MethodDescriptor[name=getClass; method=public final native java.lang.Class java.lang.Object.getClass()]
java.beans.MethodDescriptor[name=setAge; method=public void introspector.bean.User.setAge(java.lang.Integer)]
java.beans.MethodDescriptor[name=getAge; method=public java.lang.Integer introspector.bean.User.getAge()]
java.beans.MethodDescriptor[name=wait; method=public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException]
java.beans.MethodDescriptor[name=notifyAll; method=public final native void java.lang.Object.notifyAll()]
java.beans.MethodDescriptor[name=notify; method=public final native void java.lang.Object.notify()]
java.beans.MethodDescriptor[name=getUsername; method=public java.lang.String introspector.bean.User.getUsername()]
java.beans.MethodDescriptor[name=wait; method=public final void java.lang.Object.wait() throws java.lang.InterruptedException]
java.beans.MethodDescriptor[name=hashCode; method=public native int java.lang.Object.hashCode()]
java.beans.MethodDescriptor[name=setUsername; method=public void introspector.bean.User.setUsername(java.lang.String)]
java.beans.MethodDescriptor[name=wait; method=public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException]
java.beans.MethodDescriptor[name=equals; method=public boolean java.lang.Object.equals(java.lang.Object)]
java.beans.MethodDescriptor[name=toString; method=public java.lang.String introspector.bean.User.toString()]
事件描述：
```

可以看出通过内省机制可以获取 Java Bean 的属性、方法描述，这里事件描述是空的（关于事件相关会在后面介绍）。由于 Java 类都会继承 `Object` 类，可以看到这里将 `Object` 类相关的属性和方法描述也输出了，如果想将某个类的描述信息排除可以使用 

```java
java.beans.Introspector#getBeanInfo(java.lang.Class<?>, java.lang.Class<?>)
```