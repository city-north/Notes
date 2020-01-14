package vip.ericchen.study.spring.framework.aop.method;

import vip.ericchen.study.spring.framework.aop.intercept.MethodInterceptor;
import vip.ericchen.study.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * description
 *
 * @author EricChen 2020/01/13 23:37
 */
public class MethodAfterAdviceInterceptor extends AbstractAspectAdviceInterceptor {

    public MethodAfterAdviceInterceptor(Method aspectMethod, Object aspectObject) {
        super(aspectMethod, aspectObject);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
