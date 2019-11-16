package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.formatter.configuration.AppConfig;
import cn.eccto.study.springframework.tutorials.conditional.ConditionalConfiguration;
import cn.eccto.study.springframework.tutorials.conditional.serivce.LocaleService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Locale;

/**
 * description
 *
 * @author EricChen 2019/11/15 13:47
 */
public class ConditionalExample {
    public static void main(String[] args) {

        runApp(Locale.CHINA);
        System.out.println("----------");
        runApp(Locale.CANADA);

    }


    public static void runApp (Locale locale) {

        Locale.setDefault(locale);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        ConditionalConfiguration.class);
        LocaleService bean = context.getBean(LocaleService.class);
        bean.sayHello();


    }


}
