package cn.eccto.study.springframework.tutorials.dependson;

import org.springframework.context.annotation.*;

/**
 * <code>@DependsOn</code>注解使用
 *
 * @author JonathanChen 2019/11/14 20:24
 */
@Configuration
@ComponentScan("cn.eccto.study.springframework.tutorials.dependson")
class DependsOnExample {

    @Bean(initMethod = "initialize")
    @DependsOn("eventListener")//如果注释掉这一行,有可能会监听不到
    public EventPublisherBean eventPublisherBean() {
        return new EventPublisherBean();
    }

    @Bean(name = "eventListener", initMethod = "initialize")
//     @Lazy
    public EventListenerBean eventListenerBean() {
        return new EventListenerBean();
    }

    public static void main(String... strings) {
        new AnnotationConfigApplicationContext(DependsOnExample.class);
    }
}
