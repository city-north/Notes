package vip.ericchen.study.spring.framework.aop.intercept;

/**
 * description
 *
 * @author EricChen 2020/01/13 23:40
 */
public interface MethodInterceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;

}
