---
title:  EffectiveJava第7条:消除过期的对象引用
date:  2019-03-04 21:28:17
tags: effective-java
---

## 中心思想
在对象过期时及时清空引用。


## 栈的例子
```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INTIAL_CAPACITY = 16;

    public Stack(){
        elements = new Object[DEFAULT_INTIAL_CAPACITY];
    }

    public void push(Object e){
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop(){
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    //确保有足够的空间添加一个新的元素
    private void ensureCapacity(){
        if (elements.length == size)
            elements = Arrays.copyOf(elements,2 * size + 1);
    }
}
```

栈主键增长，当pop时，主键收缩，那么，从栈中弹出来的对象并不会被当成垃圾回收，即使使用栈的程序不在引用这些对象，他们也不会被回收。

```java
    public Object pop(){
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }
```
被弹出的对象的对象的引用，是永远不会再被解除的引用，即“`过期引用`”，


这段程序有一个“内存泄露”，随着垃圾回收器活动的增加，或者由于内存占用的不断增加，程序性能的降低会逐渐展现出来。在极端条件下，这种内存泄露会导致磁盘交换（`Disk Paging`） ，甚至导致程序失败（`OutOfMenoryError`）

垃圾回收站不会处理这个对象，而且不会处理这个对象所引用的所有其他对象。

<!-- more -->

## 问题关键所在
- Stack 自己管理内存（manage its owm memory）
- 存储是（Storage pool）包含了elements 数组（对象引用单元，而不是引用对象本身）的元素
- 数组活动区域中的元素是已经分配的（allocated）
- 数组的其余部分是自由的（free）

但是对于垃圾回收站来说：
- elements 数组中的所有对象引用都同等有效。

修复：

```java
    public Object pop(){
        if (size == 0)
            throw new EmptyStackException();
		Object result = element[--size];
		//解除对象的引用，垃圾回收站会进行回收
		element[size] = null;
        return result;
    }
```
## 不必清空每一个对象
清空对象引用应该是一种例外，而不是一种规范行为。

应该清空对象的情况：
- 类自己管理内存，警惕内存泄露问题
- 内存泄露的另一个常见来源是缓存
一旦你把对象引用放到缓存中，它就很容易被遗忘掉从而使他不在有用之后的很长一段时间内依然保存在缓存中。另起线程清理或者在插入新值的时候删除旧值
- 监听器和其他回调
如果你实现了一个API,客户端在这个API中注册回调，却没有显式地取消注册，那么他们就会集聚。确保回调立即被当做垃圾回收的最佳办法是只保存他们的若引用。