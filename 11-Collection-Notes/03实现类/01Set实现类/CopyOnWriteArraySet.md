# CopyOnWriteArraySet

CopyOnWriteArraySet是线程安全、无序的集合，可以将它理解成线程安全的HashSet。`CopyOnWriteArraySet`和`HashSet`虽然都继承于共同的父类`AbstractSet`但是实现方式是不一样的，`HashSet`底层是通过`HashMap`锁实现，而`CopyOnWriteArraySet`通过动态数组`CopyOnWriteArrayList`实现的

- 应用场景：数据量不大，读操作远多于写操作，需要在遍历期间防止线程冲突
- 因为通常需要复制整个基础数组，所以可变操作（add()、set() 和 remove() 等等）的开销很大。
- 迭代器支持hasNext(), next()等不可变操作，但不支持可变 remove()等 操作。
- 迭代器线程安全，安全失败，每次迭代时都会讲原数组拷贝一份再遍历。

