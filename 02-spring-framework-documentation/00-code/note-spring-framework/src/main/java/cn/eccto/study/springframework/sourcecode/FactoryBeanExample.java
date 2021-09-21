package cn.eccto.study.springframework.sourcecode;

import cn.eccto.study.springframework.sourcecode.factorybean.FactoryBeanService;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * description
 *
 * @author JonathanChen 2020/01/08 10:26
 */
public class FactoryBeanExample {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("sourcecode/beanFactoryTest.xml");
        FactoryBeanService bean = context.getBean(FactoryBeanService.class);
        bean.testFactoryBean();
    }

}
