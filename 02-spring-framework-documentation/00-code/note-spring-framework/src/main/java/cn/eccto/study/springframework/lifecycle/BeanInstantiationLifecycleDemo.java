package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * SpringBean实例化后,初始化前的Aware接口回调
 * </p>
 *
 * @author EricChen 2020/11/25 23:52
 */
public class BeanInstantiationLifecycleDemo {
    public static void main(String[] args) {
        executeBeanFactory();

        System.out.println("--------------------------------");

//        executeApplicationContext();

    }

    /**
     * BeanFactory 生命周期中有三个回调
     *
     * @see org.springframework.beans.factory.BeanNameAware
     * @see org.springframework.beans.factory.BeanClassLoaderAware
     * @see org.springframework.beans.factory.BeanFactoryAware
     */
    private static void executeBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        final int i = beanDefinitionReader.loadBeanDefinitions("META-INF/dependency-lookup-context.xml");
        beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        System.out.println("Bean实例个数:" + i);
        beanFactory.preInstantiateSingletons();
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
    }

    private static void executeApplicationContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        applicationContext.setConfigLocation("META-INF/dependency-lookup-context.xml");
        applicationContext.refresh();
        User user = (User) applicationContext.getBean("user", User.class);
        System.out.println(user);
        applicationContext.close();
    }
}
