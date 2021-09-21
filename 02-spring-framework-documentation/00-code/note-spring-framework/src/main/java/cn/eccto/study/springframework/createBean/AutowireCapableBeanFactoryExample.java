package cn.eccto.study.springframework.createBean;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * AutowireCapableBeanFactoryExample
 * </p>
 *
 * @author JonathanChen 2020/10/22 19:14
 */
public class AutowireCapableBeanFactoryExample {


    public static void main(String[] args) throws Exception {
        loadWithServiceLoaderFactoryBean();
    }

    private static void loadWithServiceLoaderFactoryBean() throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AutowireCapableBeanFactoryExample.class);
        final AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        final UserFactory bean = autowireCapableBeanFactory.createBean(DefaultUserFactory.class);
        final User user = bean.createUser();
        System.out.println(user);
    }


    @Bean
    public UserFactory userFactory() {
        return new DefaultUserFactory();
    }
}