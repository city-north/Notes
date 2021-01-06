# 002-泛型参数类型-ParameterizedType

[TOC]

## 泛型参数

```java
java.lang.reflect.ParameterizedType 5.0
  ·Type getRawType()
  //获得这个参数化类型的原始类型。
  ·Type[]getActualTypeArguments()
  //获得这个参数化类型声明时所使用的类型参数。
  ·Type getOwnerType()
  //如果是内部类型，则返回其外部类型，如果是一个顶级类型，则返回null。
```

## 什么是泛型参数类型

```java
List<String> list1; // 是泛型参数类型
List list2; 				//不是泛型参数类型
Map<String, Long> map1;//是泛型参数类型
Map map2;						//不是泛型参数类型
public Map.Entry<Long, Short> map3;//是泛型参数类型
```

具有<>符号的变量是参数化类型

```java
public Map.Entry<Long, Short> map3;//是泛型参数类型
```

#### getTypeName

获得这个参数化类型的原始类型。

```java
java.util.Map$Entry<java.lang.Long, java.lang.Short>
```

#### getRawType

获得这个参数化类型声明时所使用的类型参数。

```java
interface java.util.Map$Entry
```

#### getOwnerType()

如果是内部类型，则返回其外部类型，如果是一个顶级类型，则返回null

```java
interface java.util.Map
```

#### getActualTypeArguments

获得这个参数化类型声明时所使用的类型参数。

```java
class java.lang.Long
class java.lang.Short
```

