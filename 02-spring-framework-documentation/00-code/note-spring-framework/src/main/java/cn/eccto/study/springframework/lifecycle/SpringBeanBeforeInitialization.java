package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.lifecycle.destruction.DemoBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * <p>
 * 110-SpringBean初始化前阶段
 * </p>
 *
 * @author JonathanChen 2020/12/03 21:13
 */
public class SpringBeanBeforeInitialization {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        final int i = reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-before-initialization.xml"));
        System.out.printf("加载到 %s 个 bean", i);
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                System.out.println();
                System.out.println("Object bean = " + bean.toString());
                System.out.println("Object beanName = " + beanName);
                if (bean.getClass().equals(DemoBean.class)) {
                    final DemoBean demoBean = new DemoBean();
                    demoBean.setName("changed name ");
                    return demoBean;
                }
                return null;
            }
        });
        final DemoBean bean = beanFactory.getBean(DemoBean.class);
        System.out.println("final Bean " + bean);

    }

}
