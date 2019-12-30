---
title:  EffectiveJava第8条:避免使用中介器或者清理方法
date:  2019-03-05 21:28:17
tags: effective-java
---

## 本章作者的中心思想是：
- 终结方法（Finalizer）是不可预知的，很多时候是危险的，而且一般情况下是不必要的。
- Java 9 中终结方法已经被遗弃了，但它们仍被Java类库使用，清理方法替代了终结方法
- 比起终结方法，清理方法相对安全点，但仍是不可以预知的，运行慢的，而且一般情况下是不必要的。

## 终结方法（finalizer）和清理方法（cleaner）

```java
System.gc();
System.runFinalization();
System.runFinalizersOnExit();
```

### 缺点
1. 终结方法和清理方法无法保证立刻会执行，而且不保证它们最终会运行