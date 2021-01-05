# 020-使用ClassT参数进行类型匹配

[TOC]

## 简介

有时，匹配泛型方法中的Class＜T＞参数的类型变量很有实用价值。下面是一个标准的示例：

```java
public static <T> Pair<T> makerArrayList(Class<T> c) throws IllegalAccessException, InstantiationException {
  return new Pair<>(c.newInstance(), c.newInstance());
}


public static class Pair<T> {

  private T left;
  private T right;

  public Pair(T left, T right) {
    this.left = left;
    this.right = right;
  }
}
```

Employee.class是类型Class＜Employee＞的一个对象。makePair方法的类型参数T同Employee匹配，并且编译器可以推断出这个方法将返回一个Pair＜Employee＞。