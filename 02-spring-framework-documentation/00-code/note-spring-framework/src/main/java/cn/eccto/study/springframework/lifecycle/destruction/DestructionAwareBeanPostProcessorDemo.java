package cn.eccto.study.springframework.lifecycle.destruction;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/11/27 15:49
 */
public class DestructionAwareBeanPostProcessorDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        String location = "lifecycle/destruction/lifecycle-destruction.xml";
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        final int i = reader.loadBeanDefinitions(location);
        System.out.println(String.format("加载了 %s 个 bean", i));
        //----------------本章关注的重点------------------
        beanFactory.addBeanPostProcessor(new MyDestructionAwareBeanPostProcessor());
        beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        //----------------本章关注的重点------------------

        final DemoBean demoBean = beanFactory.getBean("demoBean", DemoBean.class);
        System.out.println(demoBean);
        beanFactory.destroySingleton("demoBean");
    }

}
