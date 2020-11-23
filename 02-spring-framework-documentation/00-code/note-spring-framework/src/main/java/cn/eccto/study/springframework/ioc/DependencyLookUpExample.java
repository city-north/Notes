package cn.eccto.study.springframework.ioc;

import cn.eccto.study.springframework.User;
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
    }

    /**
     * 根据类型查找集合
     */
    private static void lookupCollectionByType(ClassPathXmlApplicationContext beanFactory) {
        final Map<String, User> beansOfType = beanFactory.getBeansOfType(User.class);
    }

    /**
     * 根据类型查询
     */
    private static void lookupByType(BeanFactory beanFactory) {
        final User bean = beanFactory.getBean(User.class);
    }

    /**
     * 懒加载
     */
    private static void lookupLazy(BeanFactory beanFactory) {
        final ObjectFactory<User> objectFactory = (ObjectFactory) beanFactory.getBean("objectFactory");
        final User object = objectFactory.getObject();
    }

    /**
     * 实时查找方式
     */
    public static void lookupInRealTime(BeanFactory beanFactory) {
        final User user = (User) beanFactory.getBean("user");
    }


}
