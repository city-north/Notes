# ConcurrentHashMap原理

>  [052-hashMap为什么线程不安全.md](052-hashMap为什么线程不安全.md) 

- ConcurentHashMap的结构
- [JDK1.7和JDK1.8的区别](#JDK1.7和JDK1.8的区别)

## HashMap为什么线程不安全

- 1.7 的 resize 方法会导致循环链路以及数据丢失
- 1.8 的put 方法会导致数据丢失

- 

## ConcurrentHashMap 锁分段技术

> CHM里有多把锁，每一把锁用于锁容器其中一部分数据，那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率，这就是ConcurrentHashMap所使用的锁分段技术，首先将数据分成一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。

`ConcurrentHashMap` 采用分段锁的思想实现并发操作，因此是线程安全的

- CHM 由多个`Segment`组成（默认16个）（Segment 的数量也是锁的并发读）
- 每个`Segment`均继承自`ReentrantLock`并单独加锁,所以每次进行加锁操作锁的都是一个Segment 
- 只要保证每个`Segment` 都是线程安全的，也就实现了全局的线程安全

#### 值得注意的是

`concurrentLevel` 参数表示并行级别，默认是16，也就是说ConcurrentHashMap 默认是16个Segment 组成； 一旦初始化就不可以更改，CHM 可以同时支持16个线程在这些不同的`Segment`即可

### JDK1.7和JDK1.8的区别

- (1) 从1.7到1.8版本，由于HashEntry从链表 变成了红黑树所以 concurrentHashMap的时间复杂度从O(n)到O(log(n))
- (4)HashEntry在1.8中称为Node,链表转红黑树的值是8 ,当Node链表的节点数大于8时Node会自动转化为TreeNode,会转换成红黑树的结构