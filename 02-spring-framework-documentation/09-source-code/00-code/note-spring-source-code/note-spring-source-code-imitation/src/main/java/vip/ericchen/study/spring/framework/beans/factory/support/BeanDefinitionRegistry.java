package vip.ericchen.study.spring.framework.beans.factory.support;

import vip.ericchen.study.spring.framework.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition 注册中心
 *
 * @author EricChen 2020/01/09 12:31
 * @see BeanDefinition
 */
public interface BeanDefinitionRegistry {
    /**
     * 注册 BeanDefinition
     *
     * @param beanName       bean 的名称
     * @param beanDefinition bean 的定义
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);


    /**
     * 移除 BeanDefinition
     *
     * @param beanName bean 的名称
     */
    void removeBeanDefinition(String beanName);


    /**
     * 获取 BeanDefinition
     *
     * @param beanName bean 的名称
     * @return BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName);


    /**
     * 是否 Bean 定义
     *
     * @param beanName bean 的名称
     * @return if this registry contains a bean definition with the given name
     */
    boolean containsBeanDefinition(String beanName);
}
