# 单一类型依赖查找-BeanFactory

---

[TOC]

## BeanFactory设计思想

BeanFactory就是Spring中的容器,最顶层的接口, 

- 其设计目的是一个可以进行依赖查找的容器

为了可以实现这个目的,声明了以下方法

**查找Bean特性**

```java
//根据Bean名称查找
BeanFactory#getBean(java.lang.String)
//根据名称和类型查找
BeanFactory#getBean(java.lang.String, java.lang.Class<T>)
//@since 2.5 根据Bean名称查找并覆盖参数
BeanFactory#getBean(java.lang.String, java.lang.Object...)
//@since 3.0 根据类型查找
BeanFactory#getBean(java.lang.Class<T>)
//@since 4.1 根据类型查找并覆盖参数
BeanFactory#getBean(java.lang.Class<T>, java.lang.Object...)
///@since 5.1 根据类型延迟查找
BeanFactory#getBeanProvider(java.lang.Class<T>)
// @since 5.1 根据类型延迟查找
BeanFactory#getBeanProvider(ResolvableType)
```

**检测Bean特性**

```java
//是否包含
BeanFactory#containsBean(String)
//是否单例
BeanFactory#isSingleton(String)
//是否多例
BeanFactory#isPrototype(String)
//类型是否匹配
BeanFactory#isTypeMatch(java.lang.String, ResolvableType)
BeanFactory#isTypeMatch(java.lang.String, java.lang.Class<?>)
//获取类型
BeanFactory#getType(String)
//获取别名
BeanFactory#getAliases(String)
```

## 单一类型依赖查找接口

单一类型依赖查找的接口的核心功能是在 BeanFactory 接口中实现的

## 根据Bean名称查找

- [根据名称查找](#根据名称查找)
- [覆盖参数查找](#覆盖参数查找)

#### 根据名称查找

```java
Object getBean(String name) throws BeansException;
```

#### 覆盖参数查找

```java
//@since 2.5 根据Bean名称查找并覆盖参数
Object getBean(String name, Object... args) throws BeansException;
```

## 根据Bean类型查找

- [实时查找](#实时查找)
- [实时查找-覆盖参数查找](#实时查找-覆盖参数查找)
- [延迟查找](#延迟查找)

#### 实时查找

```java
@since 3.0
<T> T getBean(Cla ss<T> requiredType) throws BeansException;
```

#### 实时查找-覆盖参数查找

```java
@since 4.1
<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;
```

#### 延迟查找

```java
@since 5.1
<T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);
```

```java
@since 5.1
<T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);
```

ResolvableType是处理多类型的时候使用的,我们知道Java5之后,类型不在是Class唯一的类型,Type

- Class也是Type类型的一种
- `TypeVariable`也是Type类型的一种,局部变量

```java
public class ResolvableTypeExample {

    public static void main(String[] args) throws Exception {
        new ResolvableTypeExample().example();

    }
    private HashMap<Integer, List<String>> myMap;

    public void example() throws NoSuchFieldException {
        ResolvableType t = ResolvableType.forField(getClass().getDeclaredField("myMap"));
        t.getSuperType(); // AbstractMap<Integer, List<String>>
        t.asMap(); // Map<Integer, List<String>>
        t.getGeneric(0).resolve(); // Integer
        t.getGeneric(1).resolve(); // List
        t.getGeneric(1); // List<String>
        t.resolveGeneric(1, 0); // String
    }
}
```

## 根据名称和类型查找

```java
//根据名称和类型查找
<T> T getBean(String name, Class<T> requiredType) throws BeansException;
```

