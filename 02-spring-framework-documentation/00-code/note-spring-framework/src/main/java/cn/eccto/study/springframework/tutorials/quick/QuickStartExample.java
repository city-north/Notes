package cn.eccto.study.springframework.tutorials.quick;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author JonathanChen 2019/11/05 22:23
 */
@Configuration
class QuickStartExample {
    @Bean
    public HelloWorldService createHelloWorldService() {
        return new HelloWorldServiceImpl();
    }

    @Bean
    public HelloWorldServiceClient createClient() {
        return new HelloWorldServiceClient();
    }

    public static void main(String... strings) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(QuickStartExample.class);
        HelloWorldServiceClient bean = context.getBean(HelloWorldServiceClient.class);
        bean.showMessage();
    }
}
