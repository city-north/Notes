# 030-ByteBuf的引用计数

[TOC]

## ByteBuf的引用计数

Netty的ByteBuf 的内存回收工作是通过引用计数的方式来管理的。Netty采用 “计数器”来追中ByteBuf的生命周期

- 一是对Pooled ByteBuf的支持
- 二是能够尽快的发现那些可以回收的ByteBuf（非Pooled) 以便ByteBuf的分配和销毁效率

## 计数器的规则

- 在默认情况下， 当创建完一个 ByteBuf的时候， 它的引用为1： 
- 每次调用retain方法， 计数器就会加1
- 每次调用release方法， 计数器就会减1
- 如果引用为0，再次访问这个ByteBuf对象， 就会抛出已隐藏
- 如果引用为0， 标识这个ByteBuf没有哪个进程在引用他， 占用的内存需要被回收

## 引用计数

```java
    @Test
    public void testRef()
    {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        Logger.info("after create:" + buffer.refCnt());
        buffer.retain();
        Logger.info("after retain:" + buffer.refCnt());
        buffer.release();
        Logger.info("after release:" + buffer.refCnt());
        buffer.release();
        Logger.info("after release:" + buffer.refCnt());
        //错误:refCnt: 0,不能再retain
        buffer.retain();
        Logger.info("after retain:" + buffer.refCnt());
    }
```

```
[main|ReferenceTest.testRef] |>  after create:1 
[main|ReferenceTest.testRef] |>  after retain:2 
[main|ReferenceTest.testRef] |>  after release:1 
[main|ReferenceTest.testRef] |>  after release:0 
Disconnected from the target VM, address: '127.0.0.1:59836', transport: 'socket'
```

## 报错

```
io.netty.util.IllegalReferenceCountException: refCnt: 0, increment: 1

	at io.netty.buffer.AbstractReferenceCountedByteBuf.retain0(AbstractReferenceCountedByteBuf.java:90)
	at io.netty.buffer.AbstractReferenceCountedByteBuf.retain(AbstractReferenceCountedByteBuf.java:77)
	at com.crazymakercircle.netty.bytebuf.ReferenceTest.testRef(ReferenceTest.java:28)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:160)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:69)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:220)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:53)

```

## 引用计数为0的时候， 分为两种情况

- Pooled池化的ByteBuf内存， 回收方法是， 放入可以重新分配的ByteBuf池子等待下一次分配

- Unpooled未池化的ByteBuf缓冲区

  - 如果是堆结构缓冲， 会被JVM垃圾回收期回收
  - 如果是Direct直接内存， 会调用unsafe.freeMemory回收

  