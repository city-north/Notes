package cn.eccto.study.spring_framework.formatter;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.format.support.FormattingConversionService;

import java.util.Date;

/**
 * 修改全局的注解配置
 * 1. Java 配置方式
 * 2. xml 方式
 *
 * @author EricChen 2019/11/04 18:40
 */
public class GlobalFormaterConfigExample {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("springframework/global-formatter-config-test.xml");
        FormattingConversionService conversionService = applicationContext.getBean("conversionService", FormattingConversionService.class);
        String convert = conversionService.convert(new Date(), String.class);
        System.out.println(convert);
    }

}
