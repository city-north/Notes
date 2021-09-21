package cn.eccto.study.springframework.sourcecode;

import cn.eccto.study.springframework.constraints.Student;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * XmlBeanFactory
 *
 * @see org.springframework.beans.factory.xml.XmlBeanFactory
 * @author JonathanChen 2020/01/05 20:48
 */
public class XmlBeanFactoryExample {

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource("sourcecode/beanFactoryTest.xml"));
        xmlBeanFactory.getBean("student");
        xmlBeanFactory.getBean(Student.class);
    }

}
