package vip.ericchen.study.spring.framework.context.support;

import vip.ericchen.study.spring.framework.beans.BeanWrapperImpl;
import vip.ericchen.study.spring.framework.beans.factory.config.BeanDefinition;
import vip.ericchen.study.spring.framework.beans.factory.support.BeanDefinitionRegistry;
import vip.ericchen.study.spring.framework.beans.factory.xml.GenericBeanDefinitionReader;
import vip.ericchen.study.spring.framework.stereotype.Autowired;
import vip.ericchen.study.spring.framework.stereotype.Controller;
import vip.ericchen.study.spring.framework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * ApplicationContext 最终实现类,简化 Spring 中的 GenericApplicationContext
 *
 * @author EricChen 2020/01/08 12:50
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    // 这里维护了BeanDefinitionReader , BeanDefinitionReader也维护了这个注册中心
    private final GenericBeanDefinitionReader genericBeanDefinitionReader = new GenericBeanDefinitionReader(this);
    private final String[] resourceLocations;

    /**
     * Create a new GenericApplicationContext.
     *
     * @see #refresh
     */
    public GenericApplicationContext(String... resourceLocations) {
        //使用 BeanDefinitionReader加载 BeanDefinitions
        this.resourceLocations = resourceLocations;
        //TODO 刷新 IoC容器
        refresh();
    }


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            factoryBeanInstanceCache.put(beanName, new BeanWrapperImpl(aClass.newInstance()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    protected void doAutowrited() {
        factoryBeanInstanceCache.forEach((k, v) -> {
            Object wrappedInstance = v.getWrappedInstance();
            Class<?> wrappedClass = v.getWrappedClass();
            if (!(wrappedClass.isAnnotationPresent(Controller.class) ||
                    wrappedClass.isAnnotationPresent(Service.class))) {
                return;
            }
            Field[] fields = wrappedClass.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                String autowiredBeanName = autowired.value().trim();
                if ("".equals(autowiredBeanName)) {
                    autowiredBeanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(wrappedInstance, factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void doLoadConfig() {
        this.genericBeanDefinitionReader.loadBeanDefinitions(resourceLocations);
    }
}
