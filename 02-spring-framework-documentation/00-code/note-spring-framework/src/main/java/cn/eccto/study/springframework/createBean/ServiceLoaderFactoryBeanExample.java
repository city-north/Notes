package cn.eccto.study.springframework.createBean;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ServiceLoader;

/**
 * <p>
 * {@link ServiceLoaderFactoryBean} 方式创建一个Bean
 * </p>
 *
 * @author JonathanChen 2020/10/22 18:53
 */
public class ServiceLoaderFactoryBeanExample {


    public static void main(String[] args) throws Exception {
        loadWithJavaSPI();
        loadWithServiceLoaderFactoryBean();
    }

    private static void loadWithServiceLoaderFactoryBean() throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServiceLoaderFactoryBeanExample.class);
        final ServiceLoaderFactoryBean factoryBean = applicationContext.getBean(ServiceLoaderFactoryBean.class);
        final AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        final UserFactory bean = autowireCapableBeanFactory.createBean(DefaultUserFactory.class);
        final User user = bean.createUser();
        System.out.println(user);
        final ServiceLoader<UserFactory> userFactory = (ServiceLoader<UserFactory>) factoryBean.getObject();
        for (UserFactory factory : userFactory) {
            System.out.println(factory.createUser());
        }
    }

    private static void loadWithJavaSPI() {
        ServiceLoader<UserFactory> serviceLoader = ServiceLoader.load(UserFactory.class);
        for (UserFactory userFactory : serviceLoader) {
            System.out.println(userFactory.createUser());
        }
    }

    @Bean
    public ServiceLoaderFactoryBean serviceLoaderFactoryBean() {
        final ServiceLoaderFactoryBean serviceLoaderFactoryBean = new ServiceLoaderFactoryBean();
        serviceLoaderFactoryBean.setServiceType(UserFactory.class);
        return serviceLoaderFactoryBean;
    }

}
