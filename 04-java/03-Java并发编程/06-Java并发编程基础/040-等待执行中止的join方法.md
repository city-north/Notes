# 等待执行中止的JOIN方法

我们常常需要， 等待某件事完成只有才能继续往下执行，比如多个线程加载资源，需要等到多个线程全部加载完毕之后再汇总处理

**Thread 类中有一个join方法可以做这个事情**

#### 值得注意的是

ThreadA 调用了 ThreadB 的 join 方法，ThreadA会阻塞， ThreadB会执行， 执行完毕之后ThreadA继续执行

实际上是使得线程串行化了。这种场景使用CountDownLatch 更适合

 [01-CountDownLatch.md](..\10-Java中的并发工具类\01-CountDownLatch.md) 