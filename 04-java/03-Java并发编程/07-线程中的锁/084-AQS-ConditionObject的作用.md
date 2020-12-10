# conditionObj

## 目录

------

[TOC]

## 参考

[050-Condition 接口.md](050-Condition 接口.md) 

## AQS中的Condition

Object 方法里 有 noifty 和 wait 方法， 是配合 synchronized 内置锁实现线程间同步的基础设置

> 值得注意的是，调用共享变量的 notify 和 wait 方法实现同步前，必须先获取该共享变量的内置锁，
>
> 同理
>
> 我们在调用 条件变量 Condition 的singal 和 await 方法前必须先获取条件变量对应的锁

Condition  条件变量的 singal 和 await 方法 是用来配合锁（AQS 实现的锁）实现线程同步的基础设施

他们之间的不同点在于 ， synchronized 同时只能与一个共享变量的notify 和 wait方法实现同步， 而AQS 的一个锁可以对应多个条件变量

ConditionObj 可以直接访问 AQS 对象内部的变量, Condition 是一个条件变量,每个条件变量对应一个条件队列(单向链表队列), 用来存放调用条件变量的 await 方法后被 阻塞的线程

- 条件队列的头为 firstWaiter
- 条件队列的尾元素为 lastWaiter

```java
public class ConditionExample {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        reentrantLock.lock();

        try{
            System.out.println("begin wait");
// 调用了条件变量的await方法 阻塞挂起了当前线程，当其他线程调用条件变量的singal 方法时， 被阻塞的线程才会从await方法处返回， 需要注意的是， 和调用Object 的 wait方法一样， 如果在没有获取到锁的前提下调用了条件变量的wait 方法， 会抛出 illegalMonitorStateExecption
            condition.await();
            System.out.println("end Wait");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }
        reentrantLock.lock();
        try{
            System.out.println("begin singal");
            condition.signal();
            System.out.println("end singal");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }
    }
}
```

