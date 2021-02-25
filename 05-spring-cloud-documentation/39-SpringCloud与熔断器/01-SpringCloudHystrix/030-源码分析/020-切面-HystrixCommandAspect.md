# 020-切面-HystrixCommandAspect

[TOC]

![image-20210225162929119](../../../../assets/image-20210225162929119.png)

## 切面-HystrixCommandAspect

被注解修饰的方法将会被HystrixCommand包装执行，在Hystrix中通过Aspectj切面的方式来将被注解修饰的方法进行封装调用。

```java
//HystrixCommandAspect.java
//切面定义
@Around("hystrixCommandAnnotationPointcut() || hystrixCollapserAnnotationPointcut()")
public Object methodsAnnotatedWithHystrixCommand(final ProceedingJoinPoint joinPoint) throws Throwable {
    ...
    // 通过工厂的方式构建metaHolder
    MetaHolderFactory metaHolderFactory = META_HOLDER_FACTORY_MAP.get(HystrixPointcutType.of(method));
    MetaHolder metaHolder = metaHolderFactory.create(joinPoint); //1
    HystrixInvokable invokable = HystrixCommandFactory.getInstance().create (metaHolder);
    ExecutionType executionType = metaHolder.isCollapserAnnotationPresent() ?
        metaHolder.getCollapserExecutionType() : metaHolder.getExecutionType();
    Object result;
    try {
        if (!metaHolder.isObservable()) {
            result = CommandExecutor.execute(invokable, executionType, metaHolder);
        } else {
            result = executeObservable(invokable, executionType, metaHolder);
        }
    } catch (HystrixBadRequestException e) {
        throw e.getCause() != null ? e.getCause() : e;
    } catch (HystrixRuntimeException e) {
        throw hystrixRuntimeExceptionToThrowable(metaHolder, e);
    }
    return result;
}
```

- 1) 通过MetaHolderFactory构建出被注解修饰方法中用于构建HystrixCommand必要信息集合类MetaHolder。
- 2) 根据MetaHolder通过HystrixCommandFactory构建出合适的HystrixCommand。
- 3) 委托CommandExecutor执行HystrixCommand，得到结果。

