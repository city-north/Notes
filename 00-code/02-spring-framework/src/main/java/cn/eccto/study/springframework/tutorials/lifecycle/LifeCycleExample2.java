package cn.eccto.study.springframework.tutorials.lifecycle;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/11/14 20:25
 */
@Configuration
class LifeCycleExample2 {

    @Bean(initMethod = "myPostConstruct", destroyMethod = "cleanUp")
    public MyBean2 myBean2() {
        return new MyBean2();
    }

    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(LifeCycleExample2.class);

        context.registerShutdownHook();//注册 shutdown hook 后才会调用 销毁方法

        System.out.println("-- accessing bean --");
        MyBean2 bean = context.getBean(MyBean2.class);
        bean.doSomething();

        System.out.println("-- finished --");
    }
}