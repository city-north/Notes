package cn.eccto.study.springframework.beandefination;

import cn.eccto.study.springframework.ioc.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/10/22 15:30
 */
public class BeanDefinitionRegisterExample {
    public static void main(String[] args) {
        registerByAnnotation();
    }
    private static void registerByAnnotation() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(Config.class);
        applicationContext.refresh();
        final Config bean = applicationContext.getBean(Config.class);
        System.out.println(bean);
    }

    public static class Config {
        @Bean
        public User user() {
            return new User().setAge(11).setName("EricChen");
        }
    }
}
