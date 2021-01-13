# 050-LinkedBlockingQueue

[TOC]

## LinkedBlockingQueue说明

链表实现的有界阻塞 FIFO 队列, 此队列的默认和最大长度为 **Integer.MAX_VALUE**。

`LinkedBlockingQueue` 的内部是通过**单向链表**实现的,使用**头节点,尾结点**来进行入队和出队操作,也就是

- 入队操作都是对尾结点进行操作 

- 出队列都是对头结点进行操作

对**头结点** 和 **尾结点** 使用独占锁从而确保了原子性 , 所以出栈 和 入栈可以同时进行的, 另外对于头节点和尾结点都配对了**条件队列**, 用来存放被阻塞的线程, 并结合入队、出队操作实现了一个生产消费模型



![image-20200715132205808](../../../assets/image-20200715132205808.png)



## LinkedBlockingQueue简介

LinkedBlockingQueue 是并发包中的一个 **有界阻塞队列**,默认长度时 **Integer.MAX_VALUE** , 内部

维护了一个链表, 一个 head 节点指向队头 , 一个 tail 节点指向队尾 ,还有两个 ReentrantLock  , 一个是 putLock 一个是 takeLock  来确保原子性

- 在队列尾部添加元素 时,通过 putLock 控制同时只能有一个线程可以获取锁,, 其他线程必须等待
- 两个锁的条件队列分别用来存放出队和入队被阻塞的线程 , 这里使用了生产者消费者模型

#### LinkedBlockingQueue 是如何入队或者 出队的,如何保证线程安全

- 当调用 take / poll 等操作时需要获取 takeLock 锁, 从而保证同时只有一个线程可以操作链表头节点
  - 由于条件变量 notEmpty 内部的条件队列的维护使用是 takeLock 的锁状态管理机制,所以在调用notEmpty 的 await 方法 和 singal 方法前调用线程必须先获取到 takeLock 锁, 否则 会抛出 illegalMonitorStateException 异常
  - notEmpty 内部维护了一个条件队列,当线程获取到 takeLock 锁后调用 notEmpty 的 await 方法, 调用线程会被阻塞, 然后线程会被放到 notEmpty 内部的条件队列中进行等待, 知道有线程调用了 singal 方法
- 当调用 put / offer 等操作时 需要获取到 putLock 锁 ,  从而保证了只有一个线程可以操作链表尾结点
  - notFull 队列与 notEmpty 队列一致

#### 组成变量你了解吗?

```java
/** The capacity bound, or Integer.MAX_VALUE if none */
private final int capacity;
/** Current number of elements */
private final AtomicInteger count = new AtomicInteger();
/** 头节点 */
transient Node<E> head;
/** 尾巴节点 */
private transient Node<E> last;
/** take, poll 时获取的锁*/
private final ReentrantLock takeLock = new ReentrantLock();
/** 等待 take 的 队列*/
private final Condition notEmpty = takeLock.newCondition();
/** Lock held by put, offer, etc */
private final ReentrantLock putLock = new ReentrantLock();
/** Wait queue for waiting puts */
private final Condition notFull = putLock.newCondition();
```

## 源码分析

 [051-LinkedBlockingQueue源码分析.md](051-LinkedBlockingQueue源码分析.md) 