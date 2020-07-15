# ConcurrentLinkedQueue

线程安全的无界阻塞队列,其底层数据结构采用单向链表实现,对于入队和出队操作都使用 CAS 来实现线程安全

## 类图

<img src="../../../assets/image-20200715082553148.png" alt="image-20200715082553148" style="zoom: 50%;" />

## 内部结构

ConcurrentLinkedQueue 采用的是单向链表的方式实现 , 其中两个 Volatile 类型的 Node 节点分别用来存放队列的 首/ 尾 节点

在默认情况下,看无参构造函数

```java
public ConcurrentLinkedQueue() {
  head = tail = new Node<E>(null);
}
```

默认头和尾及诶单指向的 item 为 null 的 **哨兵节点**

在 Node 节点内部,维护了一个 **volatile** 变量修饰的变量 item .用来存放节点的值

- next 节点用来存放链表的下一个节点 , 从而连接为一个单向的无界链表 
- 内部使用 UnSafe 工具类提供的 CAS 算法来保证出入队列是操作链表的原子性

## 实现原理