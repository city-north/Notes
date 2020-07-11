# LockSupport

LockSupport 可以用来

- 阻塞一个线程
- 唤醒一个线程

> LockSupport 类与每个使用它的线程都会关联一个许可证,在默认情况下,调用 LockSupport 类的方法的线程是不持有许可证的
>
> LockSupport 类 是使用 Unsafe 类实现的

LockSupport 类是 Java6 引入的一个类，提供了基本的线程同步原语。LockSupport 实际上是调用了 Unsafe 类里的函数，归结到 Unsafe 里，只有两个函数

- unpark 函数为线程提供“许可(permit)”，线程调用 park 函数则等待“许可”。这个有 点像信号量，但是这个“许可”是不能叠加的，“许可”是一次性的。
- permit 相当于 0/1 的开关，默认是 0，调用一次 unpark 就加 1 变成了 1.调用一次park 会消费 permit，又会变成 0。 如果再调用一次 park 会阻塞，因为 permit 已 经是 0 了。
- 直到 permit 变成 1.这时调用 unpark 会把 permit 设置为 1.每个线程都 有一个相关的 permit，permit 最多只有一个，重复调用 unpark 不会累积





![image-20200326220911476](../../../assets/image-20200326220911476.png)

