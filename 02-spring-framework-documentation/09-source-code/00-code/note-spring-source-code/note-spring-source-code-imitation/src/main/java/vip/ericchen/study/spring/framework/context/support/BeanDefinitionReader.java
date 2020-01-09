package vip.ericchen.study.spring.framework.context.support;

import vip.ericchen.study.spring.framework.beans.factory.support.BeanDefinitionRegistry;

/**
 * BeanDefinition 读取器
 *
 * @author EricChen 2020/01/09 12:20
 */
public interface BeanDefinitionReader {

    /**
     * 获取 BeanDefinitionRegistry
     * @return BeanDefinitionRegistry
     */
    BeanDefinitionRegistry getRegistry();


    /**
     * 加载 BeanDefinitions
     * @param locations
     * @return
     */
    int loadBeanDefinitions(String locations);


}
