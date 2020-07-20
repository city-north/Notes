package cn.eccto.study.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyProxy<T> implements InvocationHandler {

    private Object target;

    public MyProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("-----------------begin-----------------");
        Object result = method.invoke(target, args);
        System.out.println("-----------------end-----------------");
        return result;
    }

}
