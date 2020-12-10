# 线程的等待与通知 - 使用wait和notify 

Java中的Object类是所有对象的父类， 鉴于继承机制， Java把所有类都需要的方法放到了Object 类里面

- wait 函数 

- notify 

  > 在共享变量上挂起的线程， 随机选择一个幸运线程执行
  >
  > 被唤醒的线程不能马上从wait 方法返回并且执行， 它必须获取到共享变量的监视器锁后才可以返回

## wait 函数

当一个线程调用一个共享变量的wait方法时， 该调用线程会被阻塞挂起，知道发生以下几件事情之一才会返回

- 其他线程调用了该共享对象的 notify 方法 或者 notifyAll 方法
- 其他线程调用了该线程的 interrupt 方法 ， 该方法会抛出 InterruptedException 异常返回

> 如果调用线程没有获取到对象的监视器锁， 则调用wait方法时调用线程会抛出 illegalMonitorStateException

#### 一个线程如何获取一个共享变量的监视器锁

- 执行 `synchronized` 同步代码块时 ，使用该共享变量作为参数

```java
sychronized(共享变量){
	//doSomething
}
```

- 调用该共享变量的方法， 并且该方法使用了`synchronized` 关键字修饰

```java
synchronized void add(int a , int b){
	// doSomething
}
```

#### 虚假唤醒

一个线程从挂起状态变为可以运行的状态（也就是被唤醒）即使该线程没有被其他线程调用notify ， notifyAll 方法进行通知， 或者被中断，或者等待超时， 这就是所谓的虚假唤醒

虽然虚假唤醒在应用实践中很少发生，但是要防患于未然， 做法就是不停地去测试该线程被唤醒的条件是否满足， 不满足则继续等待，也就是创建一个循环中调用wait 方法进行防范：

```java
//经典的调用共享变量wait方法的实例，首先通过同步块获取obj 上面的监视器锁，然后while 循环内调用obj的wait方法
synchronized (obj){
    while(条件不足){
    	obj.wait();
    }
}
```

## 代码实例

在Java程序中，`synchronized`解决了多线程竞争的问题。例如，对于一个任务管理器，多个线程同时往队列中添加任务，可以用`synchronized`加锁：

```java
class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
    }
}
```

但是`synchronized`并没有解决多线程协调的问题。

仍然以上面的`TaskQueue`为例，我们再编写一个`getTask()`方法取出队列的第一个任务：

```java
class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
    }

    public synchronized String getTask() {
        while (queue.isEmpty()) {
        //死循环,100%CPU
        }
        return queue.remove();
    }
}
```

上述代码看上去没有问题：`getTask()`内部先判断队列是否为空，如果为空，就循环等待，直到另一个线程往队列中放入了一个任务，`while()`循环退出，就可以返回队列的元素了。

但实际上`while()`循环永远不会退出。因为线程在执行`while()`循环时，已经在`getTask()`入口获取了`this`锁，其他线程根本无法调用`addTask()`，因为`addTask()`执行条件也是获取`this`锁。

因此，执行上述代码，线程会在`getTask()`中因为死循环而100%占用CPU资源。

如果深入思考一下，我们想要的执行效果是：

- 线程1可以调用`addTask()`不断往队列中添加任务；
- 线程2可以调用`getTask()`从队列中获取任务。如果队列为空，则`getTask()`应该等待，直到队列中至少有一个任务时再返回。

因此，多线程协调运行的原则就是：当条件不满足时，线程进入等待状态；当条件满足时，线程被唤醒，继续执行任务。

对于上述`TaskQueue`，我们先改造`getTask()`方法，在条件不满足时，线程进入等待状态：

```java
public synchronized String getTask() {
    while (queue.isEmpty()) {
        this.wait();
    }
    return queue.remove();
}
```

当一个线程执行到`getTask()`方法内部的`while`循环时，它必定已经获取到了`this`锁，此时，线程执行`while`条件判断，如果条件成立（队列为空），线程将执行`this.wait()`，进入等待状态。

这里的关键是：`wait()`方法必须在当前获取的锁对象上调用，这里获取的是`this`锁，因此调用`this.wait()`。

调用`wait()`方法后，线程进入等待状态，`wait()`方法不会返回，直到将来某个时刻，线程从等待状态被其他线程唤醒后，`wait()`方法才会返回，然后，继续执行下一条语句。

有些仔细的童鞋会指出：即使线程在`getTask()`内部等待，其他线程如果拿不到`this`锁，照样无法执行`addTask()`，肿么办？

**`wait()`方法调用时，会*释放*线程获得的锁**

这个问题的关键就在于`wait()`方法的执行机制非常复杂。首先，它不是一个普通的Java方法，而是定义在`Object`类的一个`native`方法，也就是由JVM的C代码实现的。其次，**必须在`synchronized`块中才能调用`wait()`方法**，因为`wait()`方法调用时，会*释放*线程获得的锁，`wait()`方法返回后，线程又会重新试图获得锁。

因此，只能在锁对象上调用`wait()`方法。因为在`getTask()`中，我们获得了`this`锁，因此，只能在`this`对象上调用`wait()`方法：

```java
public synchronized String getTask() {
    while (queue.isEmpty()) {
        // 释放this锁:
        this.wait();
        // 重新获取this锁
    }
    return queue.remove();
}
```

当一个线程在`this.wait()`等待时，它就会释放`this`锁，从而使得其他线程能够在`addTask()`方法获得`this`锁。

现在我们面临第二个问题：如何让等待的线程被重新唤醒，然后从`wait()`方法返回？答案是在相同的锁对象上调用`notify()`方法。我们修改`addTask()`如下：

```java
public synchronized void addTask(String s) {
    this.queue.add(s);
    this.notify(); // 唤醒在this锁等待的线程
}
```

注意到在往队列中添加了任务后，线程立刻对`this`锁对象调用`notify()`方法，这个方法会唤醒一个正在`this`锁等待的线程（就是在`getTask()`中位于`this.wait()`的线程），从而使得等待线程从`this.wait()`方法返回。

## 代码实例

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        var q = new TaskQueue();
        var ts = new ArrayList<Thread>();
        for (int i=0; i<5; i++) {
            var t = new Thread() {
                public void run() {
                    // 执行task:
                    while (true) {
                        try {
                            String s = q.getTask();
                            System.out.println("execute task: " + s);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            };
            t.start();
            ts.add(t);
        }
        var add = new Thread(() -> {
            for (int i=0; i<10; i++) {
                // 放入task:
                String s = "t-" + Math.random();
                System.out.println("add task: " + s);
                q.addTask(s);
                try { Thread.sleep(100); } catch(InterruptedException e) {}
            }
        });
        add.start();
        add.join();
        Thread.sleep(100);
        for (var t : ts) {
            t.interrupt();
        }
    }
}

class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
        this.notifyAll();
    }

    public synchronized String getTask() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }
        return queue.remove();
    }
}
```

这个例子中，我们重点关注`addTask()`方法，内部调用了`this.notifyAll()`而不是`this.notify()`，使用`notifyAll()`将唤醒所有当前正在`this`锁等待的线程，而`notify()`只会唤醒其中一个（具体哪个依赖操作系统，有一定的随机性）。

这是因为可能有多个线程正在`getTask()`方法内部的`wait()`中等待，使用`notifyAll()`将一次性全部唤醒。通常来说，`notifyAll()`更安全。有些时候，如果我们的代码逻辑考虑不周，用`notify()`会导致只唤醒了一个线程，而其他线程可能永远等待下去醒不过来了。

但是，注意到`wait()`方法返回时需要*重新*获得`this`锁。假设当前有3个线程被唤醒，唤醒后，首先要等待执行`addTask()`的线程结束此方法后，才能释放`this`锁，随后，这3个线程中只能有一个获取到`this`锁，剩下两个将继续等待。

再注意到我们在`while()`循环中调用`wait()`，而不是`if`语句：

```java
public synchronized String getTask() throws InterruptedException {
    if (queue.isEmpty()) {
        this.wait();
    }
    return queue.remove();
}
```

这种写法实际上是错误的，因为线程被唤醒时，需要再次获取`this`锁。多个线程被唤醒后，只有一个线程能获取`this`锁，此刻，该线程执行`queue.remove()`可以获取到队列的元素，然而，剩下的线程如果获取`this`锁后执行`queue.remove()`，此刻队列可能已经没有任何元素了，所以，要始终在`while`循环中`wait()`，并且每次被唤醒后拿到`this`锁就必须再次判断：

```java
while (queue.isEmpty()) {
    this.wait();
}
```

