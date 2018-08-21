package chapter02.proxy.jdkProxy;

import chapter02.proxy.staticProxy.ProxyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class ActionInvocationHandler implements InvocationHandler {
    private Object realObject;
    private Logger logger = LoggerFactory.getLogger(ProxyObject.class);

    public ActionInvocationHandler(Object realObject) {
        logger.info("ActionInvocationHandler 初始化");
        this.realObject = realObject;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用被代理类方法之前的逻辑");
        Object invoke = method.invoke(realObject, args);
        logger.info("调用被代理类方法之后的逻辑");
        return invoke;
    }
}
