# Task Scheduling using @Scheduled

spring 提供了方法的计划任务支持

- `@Scheduled`
- `@EnableScheduling`

## 简单使用@Scheduled 和 @EnableScheduling

```java
/**
 * 简单使用@Scheduled 和 @EnableScheduling
 *
 * @author EricChen 2019/11/24 16:59
 */
public class ScheduledExample {

    public static void main (String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);

        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n",
                Thread.currentThread().getName());
        bean.runTask();
        System.out.println("call MyBean#runTask() returned");

        //exit after 5 secs
        Thread.sleep(5000);
        System.exit(0);
    }

    @EnableScheduling
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }
    }

    private static class MyBean {

        @Scheduled(fixedRate = 1000)
        public void runTask () {
            System.out.printf("Running scheduled task " + " thread: %s, time: %s%n", Thread.currentThread().getName(),LocalTime.now());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
```

#### 输出

```java
calling MyBean#runTask() thread: main
Running scheduled task  thread: pool-1-thread-1, time: 22:15:07.996
Running scheduled task  thread: main, time: 22:15:07.997
call MyBean#runTask() returned
Running scheduled task  thread: pool-1-thread-1, time: 22:15:08.984
Running scheduled task  thread: pool-1-thread-1, time: 22:15:09.984
Running scheduled task  thread: pool-1-thread-1, time: 22:15:10.985
Running scheduled task  thread: pool-1-thread-1, time: 22:15:11.985
Running scheduled task  thread: pool-1-thread-1, time: 22:15:12.984
```

## 使用 TaskScheduler 替换默认

```java

/**
 * 使用 TaskScheduler 替换默认
 *
 * @author EricChen 2019/11/24 17:02
 */
public class ScheduledOverrideDefaultExecutorExample {

    public static void main (String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTask();
        System.out.println("call MyBean#runTask() returned");

        Thread.sleep(3000);
        System.out.println("Shutting down after 3 secs");
        ConcurrentTaskExecutor exec = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService es = (ExecutorService) exec.getConcurrentExecutor();
        es.shutdownNow();
    }

    @EnableScheduling
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }

        @Bean
        public TaskScheduler taskExecutor () {
            return new ConcurrentTaskScheduler(
                    Executors.newScheduledThreadPool(3));
        }
    }

    private static class MyBean {

        @Scheduled(fixedDelay = 1000)
        public void runTask () {
            System.out.printf("task thread: %s, time: %s%n",
                    Thread.currentThread().getName(),
                    LocalTime.now());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
```

```java
task thread: main, time: 22:37:25.202
task thread: pool-1-thread-1, time: 22:37:25.202
call MyBean#runTask() returned
task thread: pool-1-thread-1, time: 22:37:26.742
task thread: pool-1-thread-2, time: 22:37:28.244
Shutting down after 3 secs
```

## @Scheduled方法参数和返回值

- `@Scheduled`标注的方法必须 void 安徽以及不能有任何参数

因为它是周期性的，传递一个参数或者接收一个返回值是没有意义的

```java
/**
 * `@Scheduled`标注的方法必须 void 安徽以及不能有任何参数 ,运行会报错
 *
 * @author EricChen 2019/11/24 17:00
 */
@EnableScheduling
@Configuration
public class ScheduledMethodArgExample {

    @Bean
    public MyBean myBean () {
        return new MyBean();
    }
    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ScheduledMethodArgExample.class);

        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n",
                Thread.currentThread().getName());
        String s = bean.runTask("from main");
        System.out.println("call MyBean#runTask() returned");
        System.out.println("returned value: " + s);
    }



    private static class MyBean {

        @Scheduled
        public String runTask (String message) {
            System.out.printf("task thread: %s, time: %s%n",
                    Thread.currentThread().getName(),
                    LocalTime.now());
            System.out.printf("message: %s%n", message);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return "return value";
        }
    }
}
```

输出

```java
Exception in thread "main" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'myBean' defined in com.logicbig.example.ScheduledMethodArgExample$MyConfig: Initialization of bean failed; nested exception is java.lang.IllegalStateException: Encountered invalid @Scheduled method 'runTask': Only no-arg methods may be annotated with @Scheduled
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:482)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:754)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:866)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:542)
	at org.springframework.context.annotation.AnnotationConfigApplicationContext.
 
  (AnnotationConfigApplicationContext.java:84)
	at com.logicbig.example.ScheduledMethodArgExample.main(ScheduledMethodArgExample.java:14)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)
Caused by: java.lang.IllegalStateException: Encountered invalid @Scheduled method 'runTask': Only no-arg methods may be annotated with @Scheduled
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.processScheduled(ScheduledAnnotationBeanPostProcessor.java:413)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.postProcessAfterInitialization(ScheduledAnnotationBeanPostProcessor.java:283)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(AbstractAutowireCapableBeanFactory.java:422)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1588)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553)
	... 15 more
```

## 使用`SchedulingConfigurer`

```java
/**
 * 使用{@link SchedulingConfigurer} 器配置一个定时任务
 *
 * @author EricChen 2019/11/24 17:02
 */
public class SchedulingConfigurerExample {

    public static void main (String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);

        Thread.sleep(5000);
        System.out.println(" -- Exiting application after 5 secs-- ");
        System.exit(0);

    }

    @EnableScheduling
    @Configuration
    public static class MyConfig implements SchedulingConfigurer {

        @Override
        public void configureTasks (ScheduledTaskRegistrar taskRegistrar) {
            taskRegistrar.addFixedDelayTask(() -> {
                System.out.println("Running task : "+ LocalTime.now());
            }, 500);
        }
    }
}
```

输出

```java
Running task : 23:21:34.588
Running task : 23:21:35.093
Running task : 23:21:35.594
Running task : 23:21:36.094
Running task : 23:21:36.595
Running task : 23:21:37.096
Running task : 23:21:37.598
Running task : 23:21:38.099
Running task : 23:21:38.600
Running task : 23:21:39.101
 -- Exiting application after 5 secs--
```

