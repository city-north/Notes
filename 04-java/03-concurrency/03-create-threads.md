# Create threads

- 继承 Thread 类
- 实现 Runable 接口
- 实现 Callable 接口

## 继承 Thread 类

Thread 类本质上是实现了 Runnable 接口的一个实例，代 表一个线程的实例。启动线程的唯一方法就是通过 Thread 类的 start()实例方法。start()方法是一个 native 方法，它会 启动一个新线程，并执行 run()方法。这种方式实现多线程 很简单，通过自己的类直接 extend Thread，并复写 run() 方法，就可以启动新线程并执行自己定义的 run()方法。

```java
/**
 * <p>
 * 使用 Thread类创建线程
 * </p>
 *
 * @author EricChen 2020/02/26 23:04
 */
public class ThreadExample extends Thread {

    public ThreadExample(String name) {
        super(name);
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("测试" + getName());

        }
    }

    public static void main(String[] args) {
        new ThreadExample("TEST1").start();
        new ThreadExample("TEST2").start();
    }
}
```

## 实现 Runable 接口

如果自己的类已经 extends 另一个类，就无法直接 extends Thread，此时，可以实现一个 Runnable 接口

```java

/**
 * <p>
 * 实现 Runnable 接口的方式
 * </p>
 *
 * @author EricChen 2020/02/26 23:06
 */
public class RunnableExample implements Runnable {
    private String name;

    public RunnableExample(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("测试" + name);

        }
    }

    public static void main(String[] args) {
        new Thread(new RunnableExample("TEST1")).start();
        new Thread(new RunnableExample("TEST2")).start();
    }
}

```

## 使用 Callable接口创建线程

有的时候，我们可能需要让一步执行的线程在执行完成以 后，提供一个返回值给到当前的主线程，主线程需要依赖 这个值进行后续的逻辑处理，那么这个时候，就需要用到 带返回值的线程了。

```
/**
 * <p>
 * 使用 Callable
 * </p>
 *
 * @author EricChen 2020/02/26 23:14
 */
public class CallableExample implements Callable<String> {
    private String name;

    public CallableExample(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        int answer = 0;
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
                answer = answer + 1;
                System.out.println(name + "->" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Integer.toString(answer);

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new CallableExample("TEST1"));
        FutureTask<String> futureTask2 = new FutureTask<>(new CallableExample("TEST2"));
        new Thread(futureTask).start();
        new Thread(futureTask2).start();
    }
}

```

