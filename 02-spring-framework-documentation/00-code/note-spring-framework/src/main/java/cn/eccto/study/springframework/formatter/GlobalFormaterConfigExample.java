package cn.eccto.study.springframework.formatter;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.format.support.FormattingConversionService;

import java.util.Date;

/**
 * 修改全局的注解配置
 * 1. Java 配置方式
 * 2. xml 方式
 *
 * @author JonathanChen 2019/11/04 20:40
 */
class GlobalFormaterConfigExample {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("springframework/global-formatter-config-test.xml");
        FormattingConversionService conversionService = applicationContext.getBean("conversionService", FormattingConversionService.class);
        String convert = conversionService.convert(new Date(), String.class);
        System.out.println(convert);
    }

}
