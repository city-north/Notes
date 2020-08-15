package vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.client;



import vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.proxy.ECClassLoader;
import vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.proxy.ECInvocationHandler;
import vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.proxy.ECProxy;

import java.lang.reflect.Method;

/**
 * Created by Tom.
 */
public class ECMeipo implements ECInvocationHandler {
    private IPerson target;
    public IPerson getInstance(IPerson target){
        this.target = target;
        Class<?> clazz =  target.getClass();
        return (IPerson) ECProxy.newProxyInstance(new ECClassLoader(),clazz.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(this.target,args);
        after();
        return result;
    }

    private void after() {
        System.out.println("双方同意，开始交往");
    }

    private void before() {
        System.out.println("我是媒婆，已经收集到你的需求，开始物色");
    }
}
