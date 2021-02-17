# 050-Feign客户端实例初始化-生成Proxy接口类

[TOC]

## FeignInvocationHandler

ReflectiveFeign#newInstance方法的第二部分就是生成相应接口类的实例对象，并设置方法处理器，如下所示：

```java
//ReflectiveFeign.java
//生成Java反射的InvocationHandler
InvocationHandler handler = factory.create(target, methodToHandler);
T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class〈?〉[] {target.type()}, handler);
//将defaultMethodHandler绑定到proxy中。
for(DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
    defaultMethodHandler.bindTo(proxy);
}
return proxy;
```

OpenFeign使用Proxy的newProxyInstance方法来创建FeignClient接口类的实例，然后将InvocationHandler绑定到接口类实例上，用于处理接口类函数调用，如下所示：

```java
//Default.java
static final class Default implements InvocationHandlerFactory {
    @Override
    public InvocationHandler create(Target target, Map〈Method, MethodHandler〉 dispatch) {
        return new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
    }
}
```

Default实现了InvocationHandlerFactory接口，其create方法返回ReflectiveFeign.FeignInvocationHandler实例。

ReflectiveFeign的内部类FeignInvocationHandler是InvocationHandler的实现类，其主要作用是将接口类相关函数的调用分配给对应的MethodToHandler实例，即SynchronousMethodHandler来处理。

当调用接口类实例的函数时，会直接调用到FeignInvocationHandler的invoke方法。

invoke方法会根据函数名称来调用不同的MethodHandler实例的invoke方法，如下所示：

```java
//FeignInvocationHandler.java
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if ("equals".equals(method.getName())) {
        try {
            Object otherHandler =args.length 〉 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]): null;
            return equals(otherHandler);
        } catch (IllegalArgumentException e) {
            return false;
        }
    } else if ("hashCode".equals(method.getName())) {
        return hashCode();
    } else if ("toString".equals(method.getName())) {
        return toString();
    }
    //dispatch就是Map〈Method, MethodHandler〉，所以就是将某个函数的调用交给对应的MethodHandler来处理
    return dispatch.get(method).invoke(args);
}
```





