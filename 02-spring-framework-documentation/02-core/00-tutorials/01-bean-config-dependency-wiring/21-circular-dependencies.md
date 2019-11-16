# 循环依赖

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

## 什么是循环依赖

两个或多个 bean 在构造器互相注入

考虑下面的的代码不在 Spring Framework

```java
public  class A {
    public A(B b){
       ....
    }
}
```

```java
public  class B {
    public B(A b){
       ....
    }
}
```

在编写代码时,你不可能通过 A或者 B 的构造器去创建一个实例,你可以通过 setter 设置这个属性,但是这样会破坏在构造注入的时候,强制需要形参这个机制

在 Spring 中也有这样的问题,Spring 会抛出`BeanCurrentlyInCreationException`错误

## Spring 中的循环依赖

```java
@ComponentScan(basePackageClasses = ExampleMain.class, useDefaultFilters = false,
        //scan only the nested beans of this class
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {ExampleMain.BeanB.class, ExampleMain.BeanA.class})})
@Configuration
public class ExampleMain {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(ExampleMain.class);
    }

    @Component
    public static class BeanA {
        private final BeanB beanB;
        public BeanA(BeanB b) {
            this.beanB = b;
        }
    }

    @Component
    public static class BeanB {
        private final BeanA beanA;
        public BeanB(BeanA beanA) {
            this.beanA = beanA;
        }
    }
}
```

#### 输出

会抛出异常

```
Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'exampleMain.BeanA': Requested bean is currently in creation: Is there an unresolvable circular reference?
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.beforeSingletonCreation(DefaultSingletonBeanRegistry.java:347)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:223)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:208)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1138)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:835)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:741)
	... 33 more
```

## 使用 Setter 注入解决循环依赖问题

```java
@ComponentScan(basePackageClasses = ExampleMainFix1.class, useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {ExampleMainFix1.BeanB.class, ExampleMainFix1.BeanA.class})})
@Configuration
public class ExampleMainFix1 {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(
                        ExampleMainFix1.class);
        BeanA beanA = context.getBean(BeanA.class);
        beanA.doSomething();
    }

    @Component
    static class BeanA {
        private BeanB beanB;
        public BeanA() {
        }

        public void setB(BeanB b) {
            this.beanB = b;
        }

        public void doSomething() {
            System.out.println("doing something");
        }
    }

    @Component
    static class BeanB {
        private BeanA beanA;
        public BeanB() {
        }

        public void setBeanA(BeanA beanA) {
            this.beanA = beanA;
        }
    }
}
```

#### 输出

```
doing something
```

## 使用懒加载机制解决循环依赖问题

使用`@Lazy`注解

```java
@ComponentScan(basePackageClasses = ExampleMainFix2.class, useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {ExampleMainFix2.BeanB.class, ExampleMainFix2.BeanA.class})})
@Configuration
public class ExampleMainFix2 {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(
                        ExampleMainFix2.class);
        BeanA beanA = context.getBean(BeanA.class);
        beanA.doSomething();
    }

    @Component
    static class BeanA {
        private final BeanB beanB;
        BeanA(@Lazy BeanB b) {
            this.beanB = b;
        }

        public void doSomething() {
            beanB.doSomething();
        }
    }

    @Component
    static class BeanB {
        private final BeanA beanA;
        BeanB(BeanA beanA) {
            this.beanA = beanA;
        }

        public void doSomething() {
            System.out.println("doing something");
        }
    }
}
```

