# Node节点的属性

## 目录

------

[TOC]

## 一言蔽之

Node就是队列中维护的一个个节点

<img src="../../../assets/image-20200308221643509.png" alt="image-20200308221643509" style="zoom: 50%;" />



- 当一个线程成功的获取到同步状态(或者锁),那么其他线程就无法获取到,这个时候他们会被构建成node放到同步队列的尾端
- 当首节点释放同步状态时,将会唤醒后继节点,后继节点将会在获取同步状态成功后将自己设置为首节点

## 源码

Node 是AbstractQueuedSynchronizer中的内部类,它维护了等待的线程,以及多种状态变量用以完成

```java
static final class Node {
  static final Node SHARED = new Node();//共享模式
  static final Node EXCLUSIVE = null;//独占模式
  static final int CANCELLED =  1;
  static final int SIGNAL    = -1;
  static final int CONDITION = -2;
  static final int PROPAGATE = -3;
  volatile int waitStatus; //当前节点在队列中的状态
  volatile Node prev;  //前驱指针
  volatile Node next;  //返回前驱节点，没有的话抛出npe
  volatile Thread thread; //表示处于该节点的线程
  Node nextWaiter;//在Condition队列中的下一个等待
  
  final boolean isShared() {
    return nextWaiter == SHARED;
  }
  
  final Node predecessor() throws NullPointerException {
    Node p = prev;
    if (p == null)
      throw new NullPointerException();
    else
      return p;
  }
  Node() {    // Used to establish initial head or SHARED marker
  }
  Node(Thread thread, Node mode) {     // Used by addWaiter
    this.nextWaiter = mode;
    this.thread = thread;
  }
  Node(Thread thread, int waitStatus) { // Used by Condition
    this.waitStatus = waitStatus;
    this.thread = thread;
  }
}
```

## 值得关注的属性值

| 方法和属性值 | 含义                              |
| :----------- | :-------------------------------- |
| waitStatus   | 当前节点在队列中的状态            |
| thread       | 表示处于该节点的线程              |
| prev         | 前驱指针                          |
| predecessor  | 返回前驱节点，没有的话抛出npe     |
| nextWaiter   | 指向下一个处于CONDITION状态的节点 |
| next         | 后继指针                          |

## waitStatus等待状态

几个枚举值：

| 枚举      | 含义                                           |
| :-------- | :--------------------------------------------- |
| 0         | 当一个Node被初始化的时候的默认值               |
| CANCELLED | 为1，表示线程获取锁的请求已经取消了            |
| CONDITION | 为-2，表示节点在等待队列中，节点线程等待唤醒   |
| PROPAGATE | 为-3，当前线程处在SHARED情况下，该字段才会使用 |
| SIGNAL    | 为-1，表示线程已经准备好了，就等资源释放了     |

## 线程两种锁的模式

| 模式      | 含义                           |
| :-------- | :----------------------------- |
| SHARED    | 表示线程以共享的模式等待锁     |
| EXCLUSIVE | 表示线程正在以独占的方式等待锁 |

