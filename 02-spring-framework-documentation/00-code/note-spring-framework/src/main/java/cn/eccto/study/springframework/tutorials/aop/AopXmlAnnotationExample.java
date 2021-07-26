package cn.eccto.study.springframework.tutorials.aop;

import cn.eccto.study.springframework.tutorials.aop.service.HelloAspectService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Example of Spring Aop features , using xml config file
 *
 * @author JonathanChen 2020/01/10 19:22
 */
public class AopXmlAnnotationExample {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"tutorials/aop/application-aop.xml"});
        HelloAspectService helloAspectService = context.getBean("helloAspectService", HelloAspectService.class);
        helloAspectService.hello();
    }
}
