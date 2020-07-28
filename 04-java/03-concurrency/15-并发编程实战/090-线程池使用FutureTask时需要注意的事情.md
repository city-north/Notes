# 线程池使用 FutureTask 时需要注意的事情

线程池使用 FutureTask 时,如果把拒绝策略设置为 

- DiscardPolicy
- DiscardOldestPolicy

并在被拒绝的任务的 Future 对象上调用无参数 get 方法,那么调用**线程会一直被阻塞** ,

解决办法,在日常开发中尽量使用带超时参数的 get 方法以避免线程一直阻塞

## 问题复现

```java
public class FutureTest {
    /**
     * ① 创建了一个单线程和一个队列元素个数为 1 的线程池,拒绝策略为 DiscardPolicy
     */
    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(1,
            1,
            1L,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future futureOne = null;
        Future future2 = null;
        //②添加任务 one. 并且这个任务会由 2 唯一的线程来执行,并打印后阻塞该线程 5 秒
        futureOne = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start runnable one");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // ③ 想线程池提交了一个任务 two , 这个时候会把任务 two 放入阻塞队列
        future2 = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start runnable two");
            }
        });
        // ④ 向线程池提交任务 three , 由于队列已满,所以触发拒绝策略丢弃任务 Three
        Future futureThree = null;
        try {
            futureThree = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("start runnable three");
                }
            });
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println("task 1" + futureOne.get());
        System.out.println("task 2" + future2.get());
        System.out.println("task 3" + (futureThree == null ? null : futureThree.get()));

        executorService.shutdown();
    }
}

```

- ① 创建了一个单线程和一个队列元素个数为 1 的线程池,拒绝策略为 DiscardPolicy

- ②添加任务 one. 并且这个任务会由 2 唯一的线程来执行,并打印后阻塞该线程 5 秒
- ③ 想线程池提交了一个任务 two , 这个时候会把任务 two 放入阻塞队列
- ④ 向线程池提交任务 three , 由于队列已满,所以触发拒绝策略丢弃任务 Three
- 从任务 one 阻塞的 5s 内,主线程执行到了代码⑤,并等待任务 one执行完毕,当任务 one 执行完毕后代码⑤返回,主线程打印 task one null, 任务 one 执行完成后线程池的唯一线程会去队列里面取出任务 two 并执行,所以输出 start runnable two , 然后代码⑥ 返回
- 这时候主线程输出 task tow nul, 然后执行代码 ⑦等待任务 three执行完毕, 从执行结果看,代码 ⑦会一直阻塞而不会返回,至此 问题产生,
  - 如果把拒绝策略改为 DiscardOldestPolicy , 也会存在有一个任务的 get 方法一直阻塞,
  - 如果设置拒绝策略为 AbortPolicy 则会正常返回

## 问题分析

要分析这个问题,需要看线程池的 submit 方法都做了什么, submit 方法的代码如下

```java
    public Future<?> submit(Runnable task) {
			....
        // 装饰 Runnable为 Future 对象
        RunnableFuture<Void> ftask = newTaskFor(task, null);
        execute(ftask);
      // ⑥ 返回 Future 对象
        return ftask;
    }
```

问题出现主要是在拒绝任务的影响

DiscardPolicy 的代码

```java
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        }
```

可以看到拒绝策略什么都没做 ,返回的 Future 的状态还是 NEW 

而当我们调用 Future 的无参get 方法时 Future只有 在 COMPLETING 状态才会返回,所以会等待状态改变

```java
    public V get() throws InterruptedException, ExecutionException {
        int s = state;
        if (s <= COMPLETING)
            s = awaitDone(false, 0L);
        return report(s);
    }

    private V report(int s) throws ExecutionException {
        Object x = outcome;
      //状态为 NORMAL 正常返回
        if (s == NORMAL)
            return (V)x;
      //状态值大于等待与 CANCELLED 则抛出异常
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException((Throwable)x);
    }
```

也就是说 Future 的状态 >  COMPLETING 时调用 get方法才会返回 

- DiscardPolicy
- DiscardOldestPolice 

策略在拒绝元素时并没有设置 Feture 的状态,所以一直是 new, 所以会一直卡死

#### 使用 AbortPolicy 就没问题

因为 AbortPolicy 当线程池满的时候,会直接抛出异常 

```java
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                                                 " rejected from " +
                                                 e.toString());
        }
```

也就是 submit 方法并没有返回 Future 对象, 这个时候 future返回的实际是 null

