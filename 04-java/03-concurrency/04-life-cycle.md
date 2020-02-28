# 生命周期

Java 线程既然能够创建，那么也势必会被销毁，所以线程 是存在生命周期的，那么我们接下来从线程的生命周期开 始去了解线程。

线程一共有 6 种状态(NEW、RUNNABLE、BLOCKED、 WAITING、TIME_WAITING、TERMINATED)

- NEW:初始状态，线程被构建，但是还没有调用 start 方法 RUNNABLED:运行状态，JAVA 线程把操作系统中的就绪 和运行两种状态统一称为“运行中”

- BLOCKED:阻塞状态，表示线程进入等待状态,也就是线程 因为某种原因放弃了 CPU 使用权，阻塞也分为几种情况
  - 等待阻塞:运行的线程执行 wait 方法，jvm 会把当前 线程放入到等待队列
  - 同步阻塞:运行的线程在获取对象的同步锁时，若该同 步锁被其他线程锁占用了，那么 jvm 会把当前的线程 放入到锁池中
  - 其他阻塞:运行的线程执行 Thread.sleep 或者 t.join 方 法，或者发出了 I/O 请求时，JVM 会把当前线程设置 为阻塞状态，当 sleep 结束、join 线程终止、io 处理完 毕则线程恢复

- TIME_WAITING:超时等待状态，超时以后自动返回 TERMINATED:终止状态，表示当前线程执行完毕

## 线程的终止

线程的启动过程大家都非常熟悉，但是如何终止一个线程 呢? 这是面试过程中针对 3 年左右的人喜欢问到的一个 题目。
线程的终止，并不是简单的调用 stop 命令去。虽然 api 仍 然可以调用，但是和其他的线程控制方法如 suspend、 resume 一样都是过期了的不建议使用，就拿 stop 来说， stop 方法在结束一个线程时并不会保证线程的资源正常释 放，因此会导致程序可能出现一些不确定的状态。 要优雅的去中断一个线程，在线程中提供了一个 interrupt 方法

#### interrupt 方法

当其他线程通过调用当前线程的 interrupt 方法，表示向当 前线程打个招呼，告诉他可以中断线程的执行了，至于什 么时候中断，取决于当前线程自己。 线程通过检查资深是否被中断来进行相应，可以通过 isInterrupted()来判断是否被中断。

```
public class InterruptDemo {
    private static int i;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { //默认情况下isInterrupted 返回 false、通过 thread.interrupt 变成了 true
                i++;
            }
            System.out.println("Num:" + i);
        }, "interruptDemo");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt(); //加和不加的效果

    }
}
```

