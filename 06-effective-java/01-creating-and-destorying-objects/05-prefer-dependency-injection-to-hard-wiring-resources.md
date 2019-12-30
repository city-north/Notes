---
title:  EffectiveJava第5条:使用依赖注入而不是硬链接资源
date:  2019-03-02 21:28:17
tags: effective-java
---

## 1 总体思想
### 1.1本条作者的中心思想是：

当一个类需要以来底层的资源时，而且这些资源的行为会影响到这个类时，**不要使用** **Singleton** 或者**静态工厂类实现**，也不要让类自己去创建这些资源。
推荐的做法：
将这些资源或者创建资源的工厂传入构造器（也可以是静态工厂或者Builder）。

### 1.2 为什么要这么做：
- 提高类的灵活性
- 提高类的复用性
- 提高类的可测试性


<!-- more -->

## 2.解释中心思想
看作者给出的例子：

假如我们要写一个拼写检查器，拼写检查器（SpellChecker）一般都要依赖字典（dictionary），我们常常会看到将拼写检查器做成一个静态类方便调用。

```java
// Inappropriate use of static utility - inflexible & untestable!
public class SpellChecker {
    private static final Lexicon dictionary = ...;
    private SpellChecker() {} // Noninstantiable
    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```
我们可以看到，这个`SpellChecker` 依赖了一个`dictionary`字典，并且在`SpellChecker`内部将`dictionary`初始化了，这样很显然不够灵活，万一我要换一个字典呢？

第二种常见的误区：将这种类做成一个单例类：
```java

// Inappropriate use of singleton - inflexible & untestable!
public class SpellChecker {
    private final Lexicon dictionary = ...;
    private SpellChecker(...) {}
    public static INSTANCE = new SpellChecker(...);
    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```
这里的两种实现方式都存在致命的问题：不能更换字典，
有人说，将`dictionary `不使用final修饰，然后给它一个更改`dictionary `的方法不就行了吗？这种方式的缺点是在不适用在并发条件下

可以看出第一条结论：
**Static utility classes and singletons are inappropriate for classes whose behavior is parameterized by an underlying resource.**

**静态工具类和Singleton对于类行为需要被底层资源参数化的场景是不适用的。**

这时候作者提出了解决办法：**依赖注入**
最典型的例子就是在创建一个新的实例的时候，通过资源参数传入构造器，

```java
// Dependency injection provides flexibility and testability
public class SpellChecker {
    private final Lexicon dictionary;
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    } 
    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```
这么一看，不就是我们天天使用的方式吗？它很简单，以至于我们都不知道它有个名字叫依赖注入。

这种模式有一个辩题，就是往构造器里传一个静态工厂：Java8提供了一个很好的接口`Supplier<T> `接口，就能够很好的表现这些接口。

```java
SpellChecker create(Supplier<? extends Lexicon> dictionary) { ... }
```
这里可以看到使用了有界通配符来限制工厂的参数类型：`Supplier<? extends Lexicon>`它只允许客户端能传入一个工厂，而这个工厂能床架指定类型的任意子类型实例。

## 依赖注入的问题

虽然依赖注入提高了灵活性和可测试性，但是在大型项目中会产生混乱，这些项目往往包含了成千上万个依赖。这个时候**Spring**就出现了，这类依赖注入框架可以减少这些混乱，至于Spring是如何实现依赖注入的，将在后续的Spring框架学习中展示。

