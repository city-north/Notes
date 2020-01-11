package vip.ericchen.study.spring.framework.beans.factory.support;

import java.util.Properties;

/**
 * Simple interface for bean definition readers . Specifies load method with Resource and String location parameters
 * bean definition reader 的接口
 * <p>
 * 简化了 Spring 中的BeanDefinitionReader 和 AbstractBeanDefinitionReader
 *
 * @author EricChen 2020/01/08 12:36
 */
public interface BeanDefinitionReader {


    /**
     * Load BeanDefinition from the specified resource locations
     *
     * @param locations the resource locations
     * @return the number of bean definitions found
     */
    void loadBeanDefinitions(String[] locations);


    /**
     * Load BeanDefinition from the specified resource location
     *
     * @param location the resource location
     * @return the num
     */
    void loadBeanDefinitions(String location);


    String getProperty(String key);

}
