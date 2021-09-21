package cn.eccto.study.springframework.lifecycle;

import cn.eccto.study.springframework.lifecycle.destruction.DemoBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;

import java.util.stream.Stream;

/**
 * <p>
 * 090-SpringBean属性赋值前阶段
 * </p>
 *
 * @author JonathanChen 2020/12/03 21:46
 */
public class SpringBeanPostPopulateDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        final int i = reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-before-initialization.xml"));
        System.out.printf("加载到 %s 个 bean", i);
        beanFactory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessor() {
            @Override
            public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
                Stream.of(pvs).forEach(System.out::println);
                // 对 "DemoBean" Bean 进行拦截
                if (ObjectUtils.nullSafeEquals("demoBean", beanName) && DemoBean.class.equals(bean.getClass())) {
                    // 假设 <property name="number" value="1" /> 配置的话，那么在 PropertyValues 就包含一个 PropertyValue(number=1)
                    final MutablePropertyValues propertyValues;
                    if (pvs instanceof MutablePropertyValues) {
                        propertyValues = (MutablePropertyValues) pvs;
                    } else {
                        propertyValues = new MutablePropertyValues();
                    }
                    // 原始配置 <property name="description" value="The user holder" />
                    propertyValues.add("description", "123");
                    // 如果存在 "description" 属性配置的话
                    if (propertyValues.contains("description")) {
                        // PropertyValue value 是不可变的
                        propertyValues.removePropertyValue("description");
                        propertyValues.addPropertyValue("description", "The user holder V2");
                    }
                    return propertyValues;
                }
                return null;
            }
        });
        System.out.println(beanFactory.getBean(DemoBean.class));
    }

}
