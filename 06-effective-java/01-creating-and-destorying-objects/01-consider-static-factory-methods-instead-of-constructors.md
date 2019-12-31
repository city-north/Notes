---
title: EffectiveJava第1条:考虑使用工厂方法替代构造器
date: 2019-02-26 21:23:36
tags: effective-java
---

本条目中指出的静态工厂方法，并不是直接对应于设计模式中的工厂方法。

## 【静态工厂方法】对于【构造器】的优势
1. 静态工厂方法有名称
1. 静态工厂方法不必在每次调用他们的时候创建一个新对象。
1. 静态工厂方法可以返回原返回类型的任何子类型对象。

## 缺点
1. **类如果不含公有的或者受保护的构造函数，就不能被子类化。**
	对于共有的静态工厂所返回的非共有类，也同样如此，例如，想要实例化Collections Framework中的任何一个子类化，是不可能的
1. **他们与其他的静态方法没有任何区别**
	静态方法代表了一种对规范的背离，对于提供了静态工厂方法而不是构造方法的类来说，要想在类文档中说明如何实例化一个类，这是非常困难的。如果遵循标准的命名习惯，这个缺点可以降至最小：

<!-- more -->

|  方法名 |  备注 |
| ------------ | ------------ |
|  getInstance |   返回的实例是根据方法参数描述的，但是不能够说与参数具有同样的值。对于Singleton来说，该方法没有参数，并返回唯一的值 |
|  valueOf | 类型转换方法  |
|  of |  valueOf的简洁替代 |
|  newInstance |  和getInstance一样，但是newInstance能确保返回的每个实例都与所有其他实例不同 |
|  getType | 和getInstance一样，Type表示工厂方法所返回的对象类型 |
|  newType | 和getInstance一样，Type表示工厂方法处于不同的类中的时候使用，Type表示工厂方法锁返回的对象类型  |

列举：
## 第一个优势
**有名称**
有名字的静态工厂更方便，易于阅读。

例子：
BigInteger的构造器`BigInteger(int,int,Random)；`可能返回一个素数。

而静态方法：表达更加清楚。返回可能是素数。

```java
public static BigInteger probablePrime(int bitLength, Random rnd) {
	if (bitLength < 2)
		throw new ArithmeticException("bitLength < 2");

	return (bitLength < SMALL_PRIME_THRESHOLD ?
			smallPrime(bitLength, DEFAULT_PRIME_CERTAINTY, rnd) :
			largePrime(bitLength, DEFAULT_PRIME_CERTAINTY, rnd));
}
```


## 第二个优势
**不必在每次调用他们的时候创建一个新对象**
例子：

`Boolean.valueOf(boolean)`方法说明了这项技数，从来不创建对象。

静态工厂方法可以在重复调用时，返回同一个对象。
这样做的好处：
1. 它使得一个类可以保证是一个singleton。
1. 它使得非可变类可以保证“不会有两个相等的实例存在”。即当且仅当a==b的时候才有，`a.equals(b)`为true。
如果一个类确保了第二点，那么他的客户端就可以用 == 操作符来代替`equals(Object)`方法，带来实质性的性能提高

## 第三个优势
**可以返回原返回类型的任何子类型对象**。
一个灵活的应用时：一个API可以返回一个对象，同时又不使该对象的类成为共有的。
以这种方式把具体的实现类隐藏起来，可以得到一个非常简洁的API.
这项技术非常适合基于接口的框架结构，因为在这样的框架结构中，接口成为静态工厂方法的自然返回类型。

例如Collections Framework有20个使用的集合接口实现，分别提供了不可修改的集合、同步集合等等。这些实现绝大多数都是通过一个不可实例化的类（java.util.Collections）中的静态工厂方法而被导出的，所有返回对象的类都不是公有的。
