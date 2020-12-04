package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.lifecycle.demoBean.BeanInitializationDemo;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.core.io.ClassPathResource;

/**
 * <p>
 * 120-SpringBean初始化阶段
 * </p>
 */
public class SpringBeanInitializationDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        final int i = reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-initalization.xml"));
        System.out.printf("加载bean个数为 %s", i);
        System.out.println();
        //提供@PostConstruct支持
        beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        final BeanInitializationDemo bean = beanFactory.getBean(BeanInitializationDemo.class);
        System.out.println("bean:" + bean);
//       输出1 ---testPostConstruct---
//       输出2 ---afterPropertiesSet---
//       输出3 ---customInitMethod---
    }
}
