package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/11/23 21:05
 */
public class BeanMetadataConfigurationDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertiesBeanDefinitionReader definitionReader = new PropertiesBeanDefinitionReader(beanFactory);
        Resource resource = new ClassPathResource("META-INF/user.properties");
        EncodedResource encodedResource = new EncodedResource(resource, "utf-8");
        final int i = definitionReader.loadBeanDefinitions(encodedResource);
        //依赖查找
        final User bean = beanFactory.getBean(User.class);
        System.out.println(bean);
    }

}
