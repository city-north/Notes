package vip.ericchen.study.designpatterns.structural.proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理 ,用来生成源代码的工具类
 *
 * @author EricChen 2020/01/01 12:16
 */
public class JDKProxy implements InvocationHandler {

    private Object targetObject;


    public JDKProxy(Object targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * 获取被代理接口实例对象
     *
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName());
        System.out.println("before");
        Object invoke = method.invoke(targetObject, args);
        System.out.println("after");
        return invoke;
    }

}
