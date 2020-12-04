package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.lifecycle.demoBean.BeanInitializationDemo;
import cn.eccto.study.springframework.lifecycle.destruction.DemoBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * <p>
 * 130-SpringBean初始化后阶段.md
 * </p>
 *
 * @author EricChen 2020/12/04 10:48
 */
public class SpringBeanAfterInitializationDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-before-initialization.xml"));
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                System.out.println("---调用初始化后置处理器---");
                //返回null则
                return null;
            }
        });
        final BeanInitializationDemo bean = beanFactory.getBean(BeanInitializationDemo.class);
        System.out.println(bean);
    }

}
