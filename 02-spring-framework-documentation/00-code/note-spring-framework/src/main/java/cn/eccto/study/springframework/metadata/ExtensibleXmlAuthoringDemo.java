package cn.eccto.study.springframework.metadata;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/9 21:40
 */
public class ExtensibleXmlAuthoringDemo {
    public static void main(String[] args) {
        // 创建 IoC 底层容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 创建 XML 资源的 BeanDefinitionReader
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // 记载 XML 资源
        reader.loadBeanDefinitions("META-INF/user-extensible.xml");
        // 获取 User Bean 对象
        User user = beanFactory.getBean(User.class);
        System.out.println(user);
    }
}
