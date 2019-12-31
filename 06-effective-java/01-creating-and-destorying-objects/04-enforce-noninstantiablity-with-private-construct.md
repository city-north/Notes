---
title:  EffectiveJava第4条:通过私有构造器强化不可实例化的能力
date:  2019-03-01 21:28:17
tags: effective-java
---

有一些类不希望被实例化，如一些工具类，实例对它没有任何意义，
## 提供一个私有构造方法
```java
public class OfficeUtils{
	private OfficeUtils(){
		throw new AssertionError();
	}
}
```
由于显式的构造器是私有的，所以不可能在该类的外部访问它。`AssertionError`不是必须的，但是它可以避免不小心在类的内部调用构造器。