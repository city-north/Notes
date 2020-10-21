package cn.eccto.study.springframework.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * <p>
 * Spring中的依赖查找
 * </p>
 *
 * @author EricChen 2020/10/19 20:45
 */
@Slf4j
public class DependencyLookUpExample {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext beanFactory = new ClassPathXmlApplicationContext("classpath:tutorials/lookup/dependency-lookup.xml");
        //lookupInRealTime(beanFactory);
//        lookupLazy(beanFactory);
        //根据类型查找
//        lookupByType(beanFactory);
//        lookupCollectionByType(beanFactory);
        lookupCollectionByAnnotation(beanFactory);
    }

    /**
     * 根据注解查找Bean
     * @param beanFactory
     */
    private static void lookupCollectionByAnnotation(ClassPathXmlApplicationContext beanFactory) {
        final Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(Super.class);
        log.error("根据注解获取bean: {}", beansWithAnnotation);
    }

    /**
     * 根据类型查找集合
     */
    private static void lookupCollectionByType(ClassPathXmlApplicationContext beanFactory) {
        final Map<String, User> beansOfType = beanFactory.getBeansOfType(User.class);
        log.error("collection By Type {}", beansOfType);
    }

    /**
     * 根据类型查询
     */
    private static void lookupByType(BeanFactory beanFactory) {
        final User bean = beanFactory.getBean(User.class);
        log.error("根据类型查询 :" + bean);
    }

    /**
     * 懒加载
     */
    private static void lookupLazy(BeanFactory beanFactory) {
        final ObjectFactory<User> objectFactory = (ObjectFactory) beanFactory.getBean("objectFactory");
        final User object = objectFactory.getObject();
        log.error("延迟加载 :{}", object);
    }

    /**
     * 实时查找方式
     */
    public static void lookupInRealTime(BeanFactory beanFactory) {
        final User user = (User) beanFactory.getBean("user");
        log.error("实时查找: {}", user);
    }


}
