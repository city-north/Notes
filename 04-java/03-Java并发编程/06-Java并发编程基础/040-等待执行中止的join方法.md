# 040-等待执行中止的join方法

[TOC]

# 什么是Join

我们常常需要， 等待某件事完成只有才能继续往下执行，比如多个线程加载资源，需要等到多个线程全部加载完毕之后再汇总处理

- **Thread 类中有一个join方法可以做这个事情**

## 值得注意的是

ThreadA 调用了 ThreadB 的 join 方法，ThreadA会阻塞， ThreadB会执行， 执行完毕之后ThreadA继续执行

- join是一个实例方法, 不是静态方法, 需要使用线程对象去调用
- join调用时, 不是线程所指向的目标线程阻塞, 而是当前线程阻塞
- 只有等到当前线程所指向的线程执行完成, 或者超时, 当前线程才能重新恢复执行

## 详解Join的方法

join方法有3种重载方法

- void join() :  A线程等待B线程的执行结束后, A线程重新恢复执行
- void join(long mills) : A线程等待B线程执行一段时间, 最长等待时间为 millis 毫秒, 超过 mills 毫秒后, 不论是 B线程是否结束, A线程重新恢复执行

- void join(long millis, int nanos): 等待 B线程执行一段时间, 最长等待时间为 milis 毫秒, 加nanos 纳秒, 超过时间后, 不论线程B是否结束, A线程重新恢复执行

## join存在的问题

- 被合并的线程没有返回值, 所以引进了 FutureTask 机制