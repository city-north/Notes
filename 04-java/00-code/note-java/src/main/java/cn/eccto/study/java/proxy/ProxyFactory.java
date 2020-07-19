package cn.eccto.study.java.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory<T> {

    private final Class<T> proxyInterface;

    public ProxyFactory(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;
    }

    public <T> T  getProxy(MyProxy<T> myproxy){
        return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[] { proxyInterface }, myproxy);
    }
}
