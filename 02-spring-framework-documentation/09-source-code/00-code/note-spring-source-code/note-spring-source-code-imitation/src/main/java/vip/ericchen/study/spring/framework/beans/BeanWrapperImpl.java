package vip.ericchen.study.spring.framework.beans;

import vip.ericchen.study.spring.framework.beans.factory.BeanWrapper;

/**
 * description
 *
 * @author EricChen 2020/01/08 11:04
 */
public class BeanWrapperImpl implements BeanWrapper {

    /**
     * 包装实例
     */
    Object wrappedObject;

    /**
     * 原始实例
     */
    Object rootObject;

    public BeanWrapperImpl(Object instance) {
        this.rootObject = instance;
        this.wrappedObject = instance;
    }

    @Override
    public Object getWrappedInstance() {
        return wrappedObject;
    }

    @Override
    public Class<?> getWrappedClass() {
        return wrappedObject.getClass();
    }
}
