package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.lifecycle.MyBean;
import cn.eccto.study.springframework.tutorials.lifecycle.OtherBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/11/14 12:23
 */
@Configuration
class LifeCycleExample {

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(LifeCycleExample.class);

        context.registerShutdownHook();

        System.out.println("-- accessing bean --");
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();

        System.out.println("-- finished --");
    }
}