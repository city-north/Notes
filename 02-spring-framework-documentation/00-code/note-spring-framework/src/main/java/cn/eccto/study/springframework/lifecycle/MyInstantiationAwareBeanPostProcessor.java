package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.ioc.SuperUser;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/11/24 19:24
 */
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {


    /**
     * Bean创建之后执行的方法,我们可以返回一个Proxy
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals(beanName, "superClass") && SuperUser.class.equals(beanClass)) {
            System.out.println("postProcessBeforeInstantiation");
            //把配置完成 superUser Bean 覆盖
            return new SuperUser();
        }
        //保持 Spring IoC 容器的实例化操作
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return false;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return null;
    }
}
