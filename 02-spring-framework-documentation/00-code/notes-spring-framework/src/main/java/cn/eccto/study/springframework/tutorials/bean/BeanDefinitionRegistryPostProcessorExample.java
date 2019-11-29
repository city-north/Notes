package cn.eccto.study.springframework.tutorials.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这是 BeanFactoryPostProcessor 的一个子类
 * 它允许注册bean定义。
 * 它的方法`postProcessBeanDefinitionRegistry`在`BeanFactoryPostProcessor#postProcessBeanFactory`之前被调用。这个接口更关注于`BeanDefinition`注册，而不是通用的`BeanFactoryPostProcessor`。
 *
 * @author EricChen 2019/11/27 17:56
 */
@Configuration
public class BeanDefinitionRegistryPostProcessorExample implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
            throws BeansException {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(MyBean.class);
        bd.getPropertyValues().add("strProp", "my string property");
        registry.registerBeanDefinition("myBeanName", bd);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        //no op
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(BeanDefinitionRegistryPostProcessorExample.class);

        MyBean bean = (MyBean) context.getBean("myBeanName");
        bean.doSomething();
    }

    private static class MyBean {
        private String strProp;

        public void setStrProp(String strProp) {
            this.strProp = strProp;
        }

        public void doSomething() {
            System.out.println("from MyBean:  " + strProp);
        }
    }
}
