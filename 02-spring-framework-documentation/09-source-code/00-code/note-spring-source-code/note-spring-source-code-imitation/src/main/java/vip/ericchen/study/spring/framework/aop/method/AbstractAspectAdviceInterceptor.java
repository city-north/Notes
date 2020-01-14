package vip.ericchen.study.spring.framework.aop.method;

import vip.ericchen.study.spring.framework.aop.intercept.MethodInterceptor;
import vip.ericchen.study.spring.framework.aop.intercept.MethodInvocation;
import vip.ericchen.study.spring.framework.context.support.AbstractApplicationContext;

import java.lang.reflect.Method;

/**
 * description
 *
 * @author EricChen 2020/01/13 23:48
 */
public abstract class AbstractAspectAdviceInterceptor implements MethodInterceptor {

    private Method aspectMethod;
    private Object aspectObject;

    public AbstractAspectAdviceInterceptor(Method aspectMethod, Object aspectObject) {
        this.aspectMethod = aspectMethod;
        this.aspectObject = aspectObject;
    }

}
