package vip.ericchen.study.spring.framework.beans.factory.config;

/**
 * description
 *
 * @author EricChen 2020/01/09 12:46
 */
public interface BeanDefinition {


    /**
     * return the factoryBean name , if any
     */
    String getFactoryBeanName();


    /**
     * specify the factory bean to use , if any
     */
    void setFactoryBeanName(String factoryBeanName);


    /**
     * specify the bean class name of the bean definition
     */
    void setBeanClassName(String beanClassName);


    /**
     *
     * @return return the current class bean name of this bean definition
     */
    String getBeanClassName();



}
