package vip.ericchen.study.spring.framework.beans.factory;

/**
 * description
 *
 * @author EricChen 2020/01/07 22:55
 */
public interface BeanWrapper {

    /**
     * @return the wrapped instance
     */
    Object getWrappedInstance();

    /**
     * @return the type of the wrapped bean instance.
     */
    Class<?> getWrappedClass();
}
