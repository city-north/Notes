package chapter02.proxy.jdkProxy;

import chapter02.proxy.staticProxy.ProxyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class JDKProxy implements InvocationHandler {
    private Object targetObject;
    private Logger logger = LoggerFactory.getLogger(ProxyObject.class);


    public Object newProxy(Object targetObject) {//将目标对象传入进行代理
        logger.info("newProxy init()");
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);//返回代理对象
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        checkPopedom();//一般我们进行逻辑处理的函数比如这个地方是模拟检查权限
        return method.invoke(targetObject,args);      // 设置方法的返回值
    }

    //模拟检查权限的例子
    private void checkPopedom() {
        logger.info("正在检查权限");
    }
}
