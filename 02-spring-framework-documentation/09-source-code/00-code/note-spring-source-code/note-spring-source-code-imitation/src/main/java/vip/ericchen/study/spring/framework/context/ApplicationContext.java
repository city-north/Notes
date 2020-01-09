package vip.ericchen.study.spring.framework.context;

import vip.ericchen.study.spring.framework.beans.factory.BeanFactory;

/**
 * 简化 Spring 中的 ApplicationContext ,由于 Spring BeanFactory 的实现类非常复杂,在仿制学习的时候,简化,具体看笔记
 *
 * @author EricChen 2020/01/08 12:18
 */
public interface ApplicationContext extends BeanFactory {

    /**
     * 刷新 Context 的方法 实际来源于 ConfigurableApplicationContext
     */
    void refresh();
}
