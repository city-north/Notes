package vip.ericchen.study.spring.framework.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.spring.framework.beans.factory.BeanWrapper;
import vip.ericchen.study.spring.framework.beans.factory.config.BeanDefinition;
import vip.ericchen.study.spring.framework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 模仿 Spring 中的 AbstractApplicationContext ,主要作用是将 ApplicationContext 抽象类的通用逻辑封装
 * 这里封装了实际 A
 *
 * @author EricChen 2020/01/08 12:25
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private static Logger logger = LoggerFactory.getLogger(GenericApplicationContext.class);
    /**
     * Cache of unfinished FactoryBean instances: FactoryBean name to BeanWrapper.
     */
    protected final ConcurrentMap<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap();

    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 涵盖了 ApplicationContext 所有实现类的刷新通用逻辑
     */
    @Override
    public void refresh() {

        doLoadConfig();
        doAutowrited();

    }

    protected abstract void doAutowrited();


    protected abstract void doLoadConfig();


    @Override
    public Object getBean(String name) throws Exception {
        BeanWrapper beanWrapper = factoryBeanInstanceCache.get(name);
        return beanWrapper.getWrappedInstance();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        for (String beanName : factoryBeanInstanceCache.keySet()) {
            BeanWrapper beanWrapper = factoryBeanInstanceCache.get(beanName);
            Class<?> wrappedClass = beanWrapper.getWrappedClass();
            if (wrappedClass == requiredType) {
                Object wrappedInstance = beanWrapper.getWrappedInstance();
                return (T) wrappedInstance;
            }
        }
        return null;
    }

    /**
     * 模板方法模式,供子类去 实现
     * Template method which can be overridden to add context-specified refresh work
     */
    protected void onRefresh() {
        // For subclasses: do nothing by default.
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map map = new HashMap(factoryBeanInstanceCache.size());
        factoryBeanInstanceCache.forEach((k, v) -> {
            if (type.isInstance(v)) {
                map.put(k, v);
            }

        });
        return map;
    }


    @Override
    public List<String> getBeanDefinitionNames() {
        return new ArrayList<>(beanDefinitionMap.keySet());
    }


}
