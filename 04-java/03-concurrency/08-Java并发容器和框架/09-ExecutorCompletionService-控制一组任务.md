# ExecutorCompletionService-控制一组任务

## 为什么要有ExecutorCompletionService

执行一组任务,我们可以有三种方式

- [java.util.concurrent.ExecutorService#invokeAny](#invokeAny)
- java.util.concurrent.ExecutorService#invokeAll
- [ExecutorCompletionService](#ExecutorCompletionService)

## invokeAny

可以执行一个 集合的 Callable , 返回的是乱序的, 当我们对结果集的顺序不做要求的时候可以使用

```java
List<Callable<T>> task =...
List<Future<T>> results = service.invokeAny(task);
for (Future<T> result : results) {
  doSomthing(result.get());//拿到的是一个乱序的结果,不保证顺序
}
```

## invokeAll

返回结果是顺序的,但是如果第一个方法执行速度特别慢.get方法会阻塞在哪里

```java
List<Callable<T>> task =...
List<Future<T>> results = service.invokeAll(task);
for (Future<T> result : results) {
  doSomthing(result.get());//get方法会按照顺序获取,阻塞那
}
```

## ExecutorCompletionService

构建一个ExecutorCompletionService,这个服务在执行任务的同时, 其中包含已经提交的任务的执行结果的阻塞队列,会更加灵活一点

```java
ExecutorCompletionService service = new ExecutorCompletionService(executor);
for (Callable<T> task : tasks){
		service.submit(task);
}
for (int i = 0; i < task.size(); i++) {
	doSomething(sercice.take().get());
}
```

