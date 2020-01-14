package vip.ericchen.study.spring.framework.aop;

/**
 * AOP 代理
 *
 * @author EricChen 2020/01/13 21:44
 */
public interface AopProxy {

    /**
     * 获取代理类
     */
    Object getProxy();

    /**
     * 获取代理类
     */
    Object getProxy(ClassLoader classloader);
}
