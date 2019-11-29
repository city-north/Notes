package cn.eccto.study.springframework.tutorials.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 代码实例
 * 标注有 @Configuration 注解的类 实现 {@link BeanFactoryPostProcessor} 接口 实现 postProcessBeanFactory()
 * 方法可在所有 bean load 之后 ,初始化之前执行
 *
 * @author EricChen 2019/11/27 17:22
 */
@Configuration
public class BeanFactoryPostProcessorExample implements BeanFactoryPostProcessor {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanFactoryPostProcessorExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(MyBean.class);
        bd.getPropertyValues().add("strProp", "my string property");
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("myBeanName", bd);
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
