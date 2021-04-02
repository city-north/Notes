package cn.eccto.study.springframework.aop.interceptor;

import cn.eccto.study.springframework.aop.interceptor.after.AfterInterceptor;
import cn.eccto.study.springframework.aop.interceptor.before.BeforeInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen
 */
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
