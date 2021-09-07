# 080-Java-AOP拦截器模式

[TOC]

## 拦截类型

- 前置拦截（Before）
- 后置拦截（After)
- 异常拦截（Exception）
- 回环拦截  (Around)

## 前置通知模拟

```java
@FunctionalInterface
public interface BeforeInterceptor {


    /**
     * 前置通知
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数
     */
    void before(Object obj, Method method, Object... args);

}
```

## 后置通知模拟

```java
@FunctionalInterface
public interface AfterInterceptor {


    /**
     * 后置通知
     *
     * @param obj          对象
     * @param method       方法
     * @param args         参数
     * @param returnResult 返回值
     */
    void after(Object obj, Method method, Object[] args, Object returnResult);
}

```

## 测试代码

```java
public class ProxyEchoService implements InvocationHandler {

    private EchoService dedicate;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ans = null;
        if (EchoService.class.isAssignableFrom(method.getDeclaringClass())) {
            //前置通知
            final BeforeInterceptor beforeInterceptor = (obj, method1, args1) -> System.out.println("before");
            beforeInterceptor.before(dedicate, method, args);
            ans = method.invoke(dedicate, args);
            //后置通知
            AfterInterceptor afterInterceptor = (obj, method12, args12, returnResult) -> System.out.println("after , result = " + returnResult);
            afterInterceptor.after(dedicate, method, args, ans);
        }
        return ans;
    }


    public EchoService proxy(EchoService dedicate) {
        this.dedicate = dedicate;
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return (EchoService) Proxy.newProxyInstance(contextClassLoader, new Class[]{EchoService.class}, this);
    }

}
```