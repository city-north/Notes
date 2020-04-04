# 等待多线程完成的CountDownLatch

CountDownLatch 的构造函数接收一个 int 类型的参数作为计数器,你要等待 n个线程完成,就写 n,当然 n也可以是一个线程的 n 个执行步骤

- 当我们调用 CountDownLatch 的 countDown 方法的时候, n 就会减一

countdownlatch 是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完毕再执行。从命 名可以解读到 countdown 是倒数的意思，类似于我们倒计 时的概念。

countdownlatch 提供了两个方法，一个是 countDown， 一个是 await， countdownlatch 初始化的时候需要传入一个整数，在这个整数倒数到 0 之前，调用了 await 方法的 程序都必须要等待，然后通过 countDown 来倒数。

