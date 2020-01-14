package vip.ericchen.study.spring.framework.aop;

import vip.ericchen.study.spring.framework.aop.support.AdvisedSupport;

/**
 * description
 *
 * @author EricChen 2020/01/13 21:46
 */
public class CglibProxy implements AopProxy {

    public CglibProxy(AdvisedSupport aopConfig) {

    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classloader) {
        return null;
    }
}
