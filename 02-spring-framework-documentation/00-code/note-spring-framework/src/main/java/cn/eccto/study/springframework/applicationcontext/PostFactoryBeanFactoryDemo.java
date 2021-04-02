package cn.eccto.study.springframework.applicationcontext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/01/16 10:55
 */
public class PostFactoryBeanFactoryDemo {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
        context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        context.refresh();
        context.close();
    }

    static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("----- MyBeanFactoryPostProcessor- postProcessBeanFactory()------");
        }
    }


    static class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            System.out.println("----- MyBeanDefinitionRegistryPostProcessor- postProcessBeanDefinitionRegistry()------");

        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("----- MyBeanDefinitionRegistryPostProcessor- postProcessBeanFactory()------");

        }
    }



}
