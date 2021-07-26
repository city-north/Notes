package cn.eccto.study.springframework.ioc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 依赖注入的例子
 * </p>
 *
 * @author JonathanChen 2020/10/21 21:21
 */
public class DependencyInjectionExample {

    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:tutorials/lookup/dependency-injection.xml");
        injectByName(beanFactory);

        //内部内建Baen
//        injectInsideBean(beanFactory);
    }

    /**
     * 内部内建Bean
     * @param beanFactory
     */
    private static void injectInsideBean(BeanFactory beanFactory) {
        final Environment bean = beanFactory.getBean(Environment.class);
        System.out.println(bean);

    }

    private static void injectByName(BeanFactory beanFactory) {
        final UserRepository bean = beanFactory.getBean(UserRepository.class);
        //能够获取到,属于内建依赖
        final ObjectFactory<ApplicationContext> objectFactory = bean.getObjectFactory();
        //期直接获取
        final ObjectFactory bean1 = beanFactory.getBean(ObjectFactory.class);
        System.out.println(objectFactory == bean1);
        System.out.println(bean1);

    }

}
