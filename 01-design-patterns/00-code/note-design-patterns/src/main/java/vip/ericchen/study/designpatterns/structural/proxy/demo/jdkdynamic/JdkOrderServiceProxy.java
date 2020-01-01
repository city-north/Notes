package vip.ericchen.study.designpatterns.structural.proxy.demo.jdkdynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.designpatterns.structural.proxy.demo.common.IOrderService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * description
 *
 * @author EricChen 2020/01/01 15:27
 */
public class JdkOrderServiceProxy implements InvocationHandler {

    private IOrderService targetObject;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkOrderServiceProxy.class);


    public JdkOrderServiceProxy(IOrderService targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * 获取被代理接口实例对象
     */
    public IOrderService getProxy() {
        return (IOrderService) Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!"createOrder".equals(method.getName())) {
            return method.invoke(proxy, args);
        }
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(s.length() - 1);
        LOGGER.debug("当前时间为{},切换数据源为{}", s, substring);
        targetObject.setDataSource(substring);
        targetObject.createOrder();
        return null;
    }
}
