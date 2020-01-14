package vip.ericchen.study.spring.framework.aop;

import vip.ericchen.study.spring.framework.aop.intercept.MethodInvocation;
import vip.ericchen.study.spring.framework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * HD
 *
 * @author EricChen 2020/01/13 21:47
 */
public class JdkDynamicProxy implements AopProxy, InvocationHandler {
    private final AdvisedSupport advised;

    public JdkDynamicProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classloader) {
        return Proxy.newProxyInstance(classloader, this.advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> intercepts = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, proxy);
        MethodInvocation invocation = new MethodInvocation(proxy, null, method, args, this.advised.getTargetClass(), null);
        return null;
    }


}
