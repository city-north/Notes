package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.lifecycle.destruction.DemoBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 080-SpringBean实例化后阶段
 * </p>
 *
 * @author JonathanChen 2020/12/03 21:35
 */
public class SpringBeanAfterInstantiationDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        final int i = reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-before-initialization.xml"));
        beanFactory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessor() {
            @Override
            public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
                System.out.println("bean :" + bean);//bean :DemoBean{name='null'} 可以看出这里只是初始化了,没有populate
                System.out.println("beanName :" + beanName);
                if (ObjectUtils.nullSafeEquals("demoBean", beanName) && DemoBean.class.equals(bean.getClass())) {
                    DemoBean bean1 = (DemoBean) bean;
                    bean1.setName("changed bean");
                    // "user" 对象不允许属性赋值（填入）（配置元信息 -> 属性值） 这个时候就不会走populate逻辑了
                    return false;
                }
                //使用Spring配置对Bean进行配置
                return true;
            }
        });
        final DemoBean bean = beanFactory.getBean(DemoBean.class);
        System.out.println(bean);
    }
}
