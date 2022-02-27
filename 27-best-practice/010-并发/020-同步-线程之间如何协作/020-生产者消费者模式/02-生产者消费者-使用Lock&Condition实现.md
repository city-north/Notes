# 02-生产者消费者-使用Lock&Condition实现

[TOC]

## 简介

 [030-生产者-消费者模式：用流水线思想提高效率.md](../../../04-java/03-Java并发编程/01-tutorials/033-并发设计模式3/030-生产者-消费者模式：用流水线思想提高效率.md) 

## 示例

```java
public class BlockedQueue<T>{
  final Lock lock = new ReentrantLock();
  // 条件变量：队列不满  
  final Condition notFull = lock.newCondition();
  // 条件变量：队列不空  
  final Condition notEmpty = lock.newCondition();
 
  // 入队
  void enq(T x) {
    lock.lock();
    try {
      while (队列已满){
        // 等待队列不满 
        notFull.await();
      }  
      // 省略入队操作...
      // 入队后, 通知可出队
      notEmpty.signal();
    }finally {
      lock.unlock();
    }
  }
  // 出队
  void deq(){
    lock.lock();
    try {
      while (队列已空){
        // 等待队列不空
        notEmpty.await();
      }
      // 省略出队操作...
      // 出队后，通知可入队
      notFull.signal();
    }finally {
      lock.unlock();
    }  
  }
}
```

- notFull 理解 : 条件是没满, 队列已经满了, 我的线程要去"等没满", 所以是 nofull.await();

- 入队以后, 我要去通知"不空了"这个条件, 所以是 notEmpty.singnal();