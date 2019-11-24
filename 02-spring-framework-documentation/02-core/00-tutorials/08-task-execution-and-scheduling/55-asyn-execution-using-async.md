# Asynchronous execution using @Async

使用 `@Async`进行异步执行

- `@Async`标识方法为异步执行
- `@EnbaleAsync`启动异步执行

### 简单使用

```java
@EnableAsync
@Configuration
public class MyConfig {
   @Bean
   public MyBean myBean () {
       return new MyBean();
   }
}
```

#### 在 bean 方法中使用

```java
public class MyBean {

   @Async
   public void runTask () {
       System.out.printf("Running task  thread: %s%n",
                         Thread.currentThread().getName());
   }
}
```



```java
/**
 * 异步使用简单实例
 * @author EricChen 2019/11/24 16:51
 */
public class AsyncExample {
    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n", Thread.currentThread().getName());
        bean.runTask();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }
    }

    private static class MyBean {

        @Async
        public void runTask () {
            System.out.printf("Running task  thread: %s%n", Thread.currentThread().getName());
        }
    }
}
```

输出

```
calling async method from thread: main
Running task  thread: SimpleAsyncTaskExecutor-1
```

## 使用 Qualifier

```java
/**
 * 代码实例
 * 
 *  如果有多个执行器(executors) 注册为 bean 时,我们可以通过使用  @Async("qualifierValue") 解决冲突
 *
 * @author EricChen 2019/11/24 17:12
 */
@EnableAsync
@Configuration
public class AsyncExecutorWithQualifierExample {
    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    @Bean
    @Qualifier("myExecutor1")
    public TaskExecutor taskExecutor2 () {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
    }

    @Bean
    @Qualifier("myExecutor2")
    public TaskExecutor taskExecutor () {
        return new ThreadPoolTaskExecutor();
    }
    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncExecutorWithQualifierExample.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n", Thread.currentThread().getName());
        bean.runTask();
        ThreadPoolTaskExecutor exec = context.getBean(ThreadPoolTaskExecutor.class);
        exec.getThreadPoolExecutor().shutdown();
    }


    private static class MyBean {

        @Async("myExecutor2")
        public void runTask () {
            System.out.printf("Running task  thread: %s%n", Thread.currentThread().getName());
        }
    }
}
```

## 同步方法的返回值以及参数

- 返回值如果不是`Future`,会返回 null
- `@Anync`标注的方法可以有参数

```java
/**
 * 异步方法的返回值以及方法参数
 *
 * @author EricChen 2019/11/24 17:11
 */
@EnableAsync
@Configuration
public class AsyncArgAndReturnValueExample {

    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncArgAndReturnValueExample.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n", Thread.currentThread().getName());
        String s = bean.runTask("from main");
        System.out.println("call MyBean#runTask() returned");
        System.out.println("returned value: " + s);
    }



    private static class MyBean {
        @Async
        public String runTask (String message) {
            System.out.printf("Running task  thread: %s%n", Thread.currentThread().getName());
            System.out.printf("message: %s%n", message);
            System.out.println("task ends");
            return "return value";
        }
    }
}

```

输出

```java
call MyBean#runTask() returned
returned value: null
Running task  thread: SimpleAsyncTaskExecutor-1
message: from main
task ends
```

## 在 Async 方法后返回 Future 对象

```java
/**
 * 在 Async 方法后返回 Future 对象
 *
 * @author EricChen 2019/11/24 17:14
 */
@EnableAsync
@Configuration
public class AsyncReturningFutureExample {

    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    public static void main (String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncReturningFutureExample.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n", Thread.currentThread().getName());
        CompletableFuture<String> r = bean.runTask();
        System.out.println("result from task:" + r.get());
    }



    private static class MyBean {

        @Async
        public CompletableFuture<String> runTask () {
            System.out.printf("Running task  thread: %s%n",
                    Thread.currentThread().getName());

            CompletableFuture<String> future = new CompletableFuture<String>() {
                @Override
                public String get () throws InterruptedException, ExecutionException {
                    return " task result";
                }
            };
            return future;
        }
    }
}
```

```java
Running task  thread: SimpleAsyncTaskExecutor-1
result from task: task result
Disconnected from the target VM, address: '127.0.0.1:55490', transport: 'socket'
```

## 使用 AsyncConfigurer

AsyncConfigurer接口可以被一个标注有`@Configuration`的类实现并自定以一个 Executor 实现类

```java
public class AsyncConfigurerExample {
    public static void main (String[] args) throws ExecutionException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n", Thread.currentThread().getName());
        bean.runTask();
        ConcurrentTaskExecutor exec = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService es = (ExecutorService) exec.getConcurrentExecutor();
        es.shutdown();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig implements AsyncConfigurer {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }

        @Bean("taskExecutor")
        @Override
        public Executor getAsyncExecutor () {
            return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
        }

        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler () {
            return (throwable, method, objects) -> System.out.println("-- exception handler -- " + throwable);
        }
    }

    private static class MyBean {

        @Async
        public void runTask () {
            System.out.printf("Running task  thread: %s%n",
                    Thread.currentThread().getName());
            throw new RuntimeException("test exception");
        }
    }
}
```

