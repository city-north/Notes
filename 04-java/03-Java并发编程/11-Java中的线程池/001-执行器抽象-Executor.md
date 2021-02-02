# 001-执行器抽象-Executor

[TOC]

## 一言蔽之

java.util.concurrent.Executor 是 其他线程池的顶级抽象 , 抽象了执行的行为

## 执行器的源码

这个接口提供了一种将

- 任务提交
- 每个任务如何运行的机制(包括线程使用、调度等细节)

分离的方法

```java
public interface Executor {

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    void execute(Runnable command);
}

```

