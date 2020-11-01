# Future和FutureTask

- [为什么要有Future接口](#为什么要有Future接口)
- [Future接口详解](#Future接口详解)
- [使用FutureTask](#使用FutureTask)

## 为什么要有Future接口

Runnable 接口声明的任务的run方法无返回值,不支持抛出受检异常

- Runnable的实现类是Thread

Future 接口的任务有返回值且可以抛出受检异常

- Future的实现类是FutureTask

## Future接口详解

```java
package java.util.concurrent;

public interface Future<V> {
    //
  	boolean cancel(boolean mayInterruptIfRunning);
  	//是否已经取消  
  	boolean isCancelled();
  	//是否完成
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}

```

#### get

- 第一个get方法调用会被阻塞,直到计算完成,如果计算完成会立刻返回
- 第二个get方法可以设置超时时间并且可以线程中断,如果计算完成会立刻返回

#### cancel

- 如果调用cancel 时,任务还没开始,那么久永远不会开始
- 如果调用cancel 时任务正在执行中,可以通过设置mayInterruptIfRunning 是否中断

> 注意事项: 取消一个任务有两步
>
> - 找到要取消的线程
> - 中断这个线程
>
> 任务的实现必须要对中断敏感,也就是要捕获中断,并且抛弃现在在做的工作,
>
> 如果Feture方法不知道执行task的是哪个线程或者线程没有监控中断状态,取消是无效的

## 使用FutureTask

FutureTask 实现了Thread接口也同时实现了Callable接口

```java
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
      //使用Start方法调用
        new Thread(futureTask).start();
        new Thread(futureTask2).start();
    }
}

```

## Future实战

重命名

```java
package cn.eccto.study.java.concurrent.future;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;

/**
 * <p>
 * 寻找包含指定关键字的文件
 * </p>
 *
 * @author EricChen 2020/10/31 12:17
 */
public class FutureTest {
    private static String dir = "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/java/cn/eccto/study/java/concurrent";


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LeafRenameTask leafRenameTask = new LeafRenameTask(new File(dir));
        FutureTask<Integer> futureTask = new FutureTask(leafRenameTask);
        new Thread(futureTask).start();
        final Integer integer = futureTask.get();
        System.out.println(String.format("一共重命名了%s 个文件", integer));
    }
}

class LeafRenameTask implements Callable<Integer> {

    private final File directory;
    private int count;

    public LeafRenameTask(File directory) {
        this.directory = directory;
    }

    @Override
    public Integer call() throws Exception {
        final File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        List<Future<Integer>> answer = new ArrayList<>();
        Stream.of(files).forEach(file -> {
            if (file.isDirectory()) {
                LeafRenameTask leafRenameTask = new LeafRenameTask(file);
                FutureTask<Integer> futureTask = new FutureTask<>(leafRenameTask);
                answer.add(futureTask);
                new Thread(futureTask).start();
            } else {
                rename(file);

            }
        });
        for (Future<Integer> integerFuture : answer) {
            count += integerFuture.get();
        }
        return count;
    }

    private void rename(File file) {
        FileSuffix suffix = getSuffix(file);
        if (suffix == FileSuffix.OTHER) {
            return;
        }
        String absolutePath = file.getAbsolutePath();
        String targetPath = getTargetPath(absolutePath, suffix);
        System.out.println(absolutePath + " || rename to || " + targetPath);
        file.renameTo(new File(targetPath));
        count++;
    }

    private String getTargetPath(String absolutePath, FileSuffix suffix) {
        StringBuilder sb = new StringBuilder(absolutePath);
        switch (suffix) {
            case SCREEN:
                sb.replace(sb.length() - 6, sb.length(), "lview");
                break;
            case BM:
                sb.replace(sb.length() - 2, sb.length(), "lwm");
                break;
            case SVC:
                sb.replace(sb.length() - 3, sb.length(), "lsc");
                break;
        }
        return sb.toString();
    }

    private FileSuffix getSuffix(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            String name = file.getName();
            int index = name.lastIndexOf('.');
            if (index < 0 || index + 1 > name.length()) {
                return FileSuffix.OTHER;
            }
            String substring = name.substring(index + 1);
            switch (substring.toLowerCase(Locale.CHINA)) {
                case "screen":
                    return FileSuffix.SCREEN;
                case "bm":
                    return FileSuffix.BM;
                case "svc":
                    return FileSuffix.SVC;
                default:
                    return FileSuffix.OTHER;
            }
        }
        return FileSuffix.OTHER;
    }
      enum FileSuffix {
        BM(),
        SCREEN(),
        SVC(),
        OTHER();
    }

}
```

