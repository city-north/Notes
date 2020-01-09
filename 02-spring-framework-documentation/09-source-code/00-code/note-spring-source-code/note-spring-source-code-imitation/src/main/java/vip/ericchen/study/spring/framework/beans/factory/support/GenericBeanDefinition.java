package vip.ericchen.study.spring.framework.beans.factory.support;

import vip.ericchen.study.spring.framework.beans.factory.config.BeanDefinition;

/**
 * Bean 定义的标准实现,将所有父类忽略
 *
 * @author EricChen 2020/01/09 13:47
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String factoryBeanName;
    private String beanClassName;


    public GenericBeanDefinition(String factoryBeanName, String beanClassName) {
        this.factoryBeanName = factoryBeanName;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }
}
