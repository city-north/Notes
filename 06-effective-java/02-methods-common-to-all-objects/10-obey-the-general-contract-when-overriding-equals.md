---
title:  EffectiveJava第10条:当重写 equals 遵守通用约定
date:  2019-03-08 21:28:17
tags: effective-java
---

## 当重写 equals 遵守通用约定
重写 equals方法看起来比较简单,但是总是非常容易犯错,而且犯错后的结果有可能非常严重.
一般来说不用重写`equals`方法可以避免这种错误,这样的话这个实力只能跟自己相同.默认的 equals方法实现了:

- **类的每个实例本质上都是独一无二的。**这一点对于那些代表活动实体而不是值的类来说的确如此。Object类提供的equals方法实现正是这些类的正确行为。
- **对于一个类来说不需要提供一个逻辑相等的测试**例如，`java.util.regex.Pattern`本可以通过覆盖equals方法来校验两个Pattern实例是否表示相同正则表达式，但设计者们并不认为客户端需要这个功能。在这种情况下，equals方法直接继承自Object类就已经足够了。
- **父类已经重写了`equals`方法,而且对于其子类来说是通用的**,例如，大多数`Set`的`equals`方法的实现都是继承自`AbstractSet`，类似地`，List`继承自`AbstractList`，`Map`继承自`AbstractMap`。
- 如果一个类是私有的或者是包私有的，那么我们可以确定它的equals方法将永远不会被调用。如果你很害怕风险，那么你也可以覆盖equals方法来保证它不会被意外调用：

<!-- more -->
```java
@Override 
public boolean equals(Object o) {
    throw new AssertionError(); // Method is never called
}
```

那么什么时候要去重写`equals`方法呢
- 一个类有自己的逻辑相等概念而不仅仅是对象相等
- 一个类的父类没有重写`equals`方法

有一类值类（value class）不要求覆盖equals方法，这些类采用了实例控制（条目1）技术来确保同一值的对象最多只有一个。枚举类型（条目34）就属于这种。在这种情况下，逻辑相等与对象相等是等价的，所以Object的equals方法就可以作为逻辑相等方法（logical equals method）。

equals方法实现了等价关系（equivalence relation）。等价关系包含了以下几个性质：
- 自反性：对于任意非空引用值x，x.equals(x)必须返回true。
- 对称性：对于任意非空引用值x和y，x.equals(y)必须返回true，当且仅当y.equals(x)返回true。
- 传递性：对于任意非空引用值x，y，z，如果x.equals(y)返回true而且y.equals(z)也返回true，那么x.equals(z)必须返回true。
- 一致性：对于任意非空引用值x和y，只要equals方法中使用的信息没有被修改，那么不管多少次调用x.equals(y)都必须一致性地返回true或者false。
- 对于任意非空引用值x，x.equals(null)必须返回false。

### 自反性
当一个对象调用它的 equals 方法跟自己比较时,总是返回 true

### 对称性
两个对象对于它们是否相等的问题必须达成一致。与第一条要求不同，若有谁无意中违反了这一条，是不难想象的。例如，考虑下面这个类，这个类实现了一个大小写不敏感的字符串。字符串由toString保存，但在equals方法中被忽略：

```java

// Broken - violates symmetry!
public final class CaseInsensitiveString {
    private final String s;
    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    } 
    // Broken - violates symmetry!
    @Override 
    public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        if (o instanceof String) // One-way interoperability!
            return s.equalsIgnoreCase((String) o);
        return false;
    } 
    ... // Remainder omitted
}
```

上面类中的equals方法意图很好，它企图与正常的字符串进行互操作。让我们来假设我们同时有一个大小写不敏感字符串和一个普通的字符串：
```
CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
String s = "polish";
```

正如所料，`cis.equals(s)`返回了`true`。问题是，虽然`CaseInsensitiveString`类的`equals`方法知道普通的字符串，但String类的equals方法却对大小写不敏感的字符串一无所知。因此，`s.equals(cis)`返回`false`， 这显然违反了对称性。设想我们将一个大小写不敏感的字符串放入一个集合里：

```java
List<CaseInsensitiveString> list = new ArrayList<>();
list.add(cis);
```

在这个时候，list.contains(s)会返回什么？没人知道。 在当前的OpenJDK实现中，它恰巧返回了false， 但这只是一个特定实现的返回结果。在别的实现里，它有可能会返回true或抛出一个运行时异常。你一旦违反了equals约定，你将不知道别的对象在面对你的对象时会产生什么行为。
为了消除这个问题，只需将把企图与String互操作的代码从equals方法里删掉就可以了。这么做之后，你就可以将这个方法重构成一条单独的返回语句：

```java
@Override 
public boolean equals(Object o) {
    return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
}
```
### 传递性

### 一致性

