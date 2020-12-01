package cn.eccto.study.springframework.metadata;

import cn.eccto.study.springframework.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * A demo for Spring Configuration Metadata
 * </p>
 *
 * @author EricChen 2020/12/01 20:30
 */
public class BeanConfigurationMetadataDemo {


    public static void main(String[] args) {
        // BeanDefinition 的定义（声明）
        final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        beanDefinitionBuilder.addPropertyValue("name", "EricChen");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 获取 AbstractBeanDefinition
        final AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        // 附加属性（不影响 Bean populate、initialize）,主要用作上下文传递临时变量
        beanDefinition.setAttribute("name", "chengbei");
        // 当前 BeanDefinition 来自于何方（辅助作用）
        beanDefinition.setSource(BeanConfigurationMetadataDemo.class);
        beanFactory.registerBeanDefinition("user", beanDefinition);
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
                    BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
                    // 通过 source 判断来
                    if ( BeanConfigurationMetadataDemo.class.equals(bd.getSource())){
                        // 属性（存储）上下文
                        final String name = (String) bd.getAttribute("name");
                        User user = (User) bean;
                        user.setName(name);
                        return bean;
                    }
                }
                return bean;
            }
        });
        // 注册 User 的 BeanDefinition
        final User user = beanFactory.getBean("user", User.class);
        System.out.println("User = " + user);
    }


}
