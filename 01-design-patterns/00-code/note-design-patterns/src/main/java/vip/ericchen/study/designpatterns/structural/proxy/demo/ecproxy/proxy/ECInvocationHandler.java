package vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.proxy;

import java.lang.reflect.Method;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/08/15 20:57
 */
public interface ECInvocationHandler {
    Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
}
