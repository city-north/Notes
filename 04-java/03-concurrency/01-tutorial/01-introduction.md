#线程简介

##什么是线程
现代操作系统调度最小单元是线程。

一个Java程序从`main()`方法开始执行，看似只有一个线程，其实有包含多个线程。

```java
import java.lang.management.*;

public class HelloWorld {
    public static void main(String []args) {
		//获取Java线程管理MXBean
      ThreadMXBean threadMXBean  = ManagementFactory.getThreadMXBean();
		//不需要获取同步的monitor 和 synchronizer信息，仅获取线程和线程堆栈信息
	  ThreadInfo[] infos = threadMXBean.dumpAllThreads(false,false);
		//遍历线程信息，仅打印线程ID和线程名称
	  for(ThreadInfo info : infos){
        System.out.println("[" + info.getThreadId() + "]" + info.getThreadName());
	  	}
    }
}
```

输出：
```java
[4]Signal Dispatcher	//分发处理发送给JVM信号的线程
[3]Finalizer					//调用对象finalize方法的线程
[2]Reference Handler	//清除Reference的线程
[1]main								//main线程、用户程序入口
```
可以看出，一个Java程序的运行不仅仅是main()方法的运行，而是main线程和多个其他线程共同运行。

## 线程的生命周期
在给定的一个时刻，线程只能处于其中一个状态。


![](assets/06dbbdd56eec059f26ea8a40e00800a9.jpg)


## 线程状态变迁
![](assets/233cb16d0dbf48f7d8592d50b8a4669b.jpg)




- 调用`start()`方法开始运行。
- 当前线程执行`wait()`方法后，线程进入等待状态。
- 进入等待状态的线程需要依靠其他线程的通知才能够返回到运行状态。
- 超时等待状态相当于在等待状态的基础上增加了超时限制。
- 超时时间到达时将会返回到运行状态
- 当线程调用同步方法时，在没有获取锁的情况下，线程将会进入阻塞状态。
- 线程在执行Runnable 的 `run（）`方法之后将会进入到终止状态。

#### RUNNING 运行状态


- 在运行状态中,`running `状态和`ready`状态是等待系统调度的,有一个`yield()`方法,实际上是让出当前时间片,主动让出系统资源,主动到 `ready`
- 实际上`ready`状态不存在,方便理解,实际上就是 CPU 时间片的调度没有调度它的状态

#### BLOCKED 阻塞状态

- 只有在等待锁的情况下才有阻塞状态

## Daemon 线程
Daemon线程是一种支持型线程，因为它主要被用作程序中后台调度以及支持性工作。
**当一个Java虚拟机不存在非Daemon**线程时，Java虚拟机将会退出，可以通过Thread.setDaemon(true)将线程设置为Daemon线程。

Daemin线程被用作完成支持性工作，但是在Java虚拟机退出时Daemon 线程中的finally块并不一定会执行：

![](https://www.showdoc.cc/server/api/common/visitfile/sign/9dc92e7962acb31fd2ae504bec5b1176?showdoc=.jpg)


不会输出DaemonThread ...,因为main线程（非Daemon线程）在启动了线程DaemonRunner之后随着main方法执行完毕而终止，而此时Java虚拟机已经没有非Daemon线程，虚拟机需要退出，Java虚拟机中的所有Daemon线程都需要立刻终止，因此DaemonRunner立即终止，但是DaemonRunner中的finally块并没有执行。

所以：
**在构建Daemon线程时，不能依靠`finally`块中的内容确保执行关闭或者清理资源的逻辑**

