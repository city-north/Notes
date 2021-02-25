# 030-元数据信息-MetaHolder

[TOC]

![image-20210225162929119](../../../../assets/image-20210225162929119.png)

## MetaHolder构建HystrixCommand和与被包装方法相关的必要信息

MetaHolder持有用于构建HystrixCommand和与被包装方法相关的必要信息，如被注解的方法、失败回滚执行的方法和默认的命令键等属性。其属性代码如下所示：

```java
//MetaHolder.java
@Immutable
public final class MetaHolder {
    ...
    private final Method method; //被注解的方法
    private final Method cacheKeyMethod;
    private final Method ajcMethod;
    private final Method fallbackMethod; // 失败回滚执行的方法
    ...
    private final String defaultGroupKey; // 默认的group键
    private final String defaultCommandKey; // 默认的命令键
    private final String defaultCollapserKey; // 默认的合并请求键
    private final String defaultThreadPoolKey; // 默认的线程池键
    private final ExecutionType executionType; // 执行类型
    ...
}
```

在HystrixCommandFactory类中，用于创建HystrixCommand的方法如下所示：

```java
//HystrixCommandFactory.java
public HystrixInvokable create(MetaHolder metaHolder) {
  HystrixInvokable executable;
  // 构建请求合并的命令
  if (metaHolder.isCollapserAnnotationPresent()) {
    executable = new CommandCollapser(metaHolder);
  } else if (metaHolder.isObservable()) {
    executable = new GenericObservableCommand(HystrixCommandBuilderFactory.getInstance().create(metaHolder));
  } else {
    executable = new GenericCommand(HystrixCommandBuilderFactory.getInstance(). create(metaHolder));
  }
  return executable;
}
```

根据MetaHolder#isObservable方法返回属性的不同，将会构建不同的命令，比如HystrixCommand或者HystrixObservableCommand，前者将同步或者异步执行命令，后者异步回调执行命令。

Hystrix根据被包装方法的返回值来决定命令的执行方式，判断代码如下：

```java
//CommandMetaHolderFactory.java
...
ExecutionType executionType = ExecutionType.getExecutionType(method.getReturnType());
...
public enum ExecutionType {
    // 异步执行命令
    ASYNCHRONOUS,
    // 同步执行命令
    SYNCHRONOUS,
    // 响应式执行命令(异步回调)
    OBSERVABLE;
    private static final Set〈? extends Class〉 RX_TYPES = ImmutableSet.of(Observable.class, Single.class, Completable.class);
    // 根据方法的返回类型返回对应的ExecutionType
    public static ExecutionType getExecutionType(Class〈?〉 type) {
          // Future为异步执行
        if (Future.class.isAssignableFrom(type)) {
            return ExecutionType.ASYNCHRONOUS;
        } else if (isRxType(type)) {
        // 属于 rxType为异步回调执行
            return ExecutionType.OBSERVABLE;
        } else {
        // 其他为同步执行
            return ExecutionType.SYNCHRONOUS;
        }
    }
}
```

根据被包装方法的返回值类型决定命令执行的ExecutionType，从而决定构建HystrixCommand还是HystrixObservableCommand。其中Future类型的返回值将会被异步执行，rx类型的返回值将会被异步回调执行，其他的类型将会被同步执行。

CommandExecutor根据MetaHolder中ExecutionType执行类型的不同，选择同步执行、异步执行还是异步回调执行，返回不同的执行结果。同步执行，直接返回结果对象；异步执行，返回Future，封装了异步操作的结果；异步回调执行将返回Observable，封装响应式执行的结果，可以通过它对执行结果进行订阅，在执行结束后进行特定的操作。

## 



