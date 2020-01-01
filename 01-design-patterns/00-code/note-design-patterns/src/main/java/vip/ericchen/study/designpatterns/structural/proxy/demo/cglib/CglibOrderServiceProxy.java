package vip.ericchen.study.designpatterns.structural.proxy.demo.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.designpatterns.structural.proxy.demo.common.IOrderService;


public class CglibOrderServiceProxy implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CglibOrderServiceProxy.class);

    public <T> T getInstance(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }


    @Override
    public Object intercept(Object targetObject, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (!"createOrder".equals(method.getName())) {
            return methodProxy.invokeSuper(targetObject, objects);
        }
        if (!(targetObject instanceof IOrderService)) {
            return methodProxy.invokeSuper(targetObject, objects);
        }
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(s.length() - 1);
        LOGGER.debug("当前时间为{},切换数据源为{}", s, substring);
        Method setDataSource = targetObject.getClass().getMethod("setDataSource", String.class);
        setDataSource.invoke(targetObject, substring);
        methodProxy.invokeSuper(targetObject, objects);
        return null;
    }
}
