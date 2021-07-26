package cn.eccto.study.springframework.tutorials.aop;

import cn.eccto.study.springframework.tutorials.aop.service.HelloAspectService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Example of Spring AOP features , using java config instead of xml config file
 *
 * @author JonathanChen 2020/01/10 19:19
 */
public class AopAnnotationExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("cn.eccto.study.springframework.tutorials.aop");
        HelloAspectService helloAspectService = context.getBean("helloAspectService", HelloAspectService.class);
        helloAspectService.hello();
    }

}
