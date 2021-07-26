package cn.eccto.study.springframework.metadata.propertyvalues;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;


/**
 * <p>
 * TODO
 * </p>
 *
 * @author JonathanChen 2021/01/06 11:31
 */
public class PropertyValuesTest {

    public static void main(String[] args) {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        builder.addPropertyValue("name", "JonathanChen");
        final AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        defaultListableBeanFactory.registerBeanDefinition("user", beanDefinition);
        final Object user = defaultListableBeanFactory.getBean("user");
        System.out.println(user);



    }

}
