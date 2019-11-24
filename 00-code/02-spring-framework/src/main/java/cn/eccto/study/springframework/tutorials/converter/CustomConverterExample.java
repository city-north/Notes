package cn.eccto.study.springframework.tutorials.converter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 自定义 {@link Converter} 实现类以及使用方式
 *
 * @author EricChen 2019/11/24 11:43
 */
@Configuration
public class CustomConverterExample {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomConverterExample.class);
        ConversionService bean = context.getBean(ConversionService.class);
        LocalDateTime convert = bean.convert(new Date(), LocalDateTime.class);
        System.out.println(convert);
        String s = bean.convert(new Date(), String.class);
        System.out.println(s);
    }

    @Bean
    public ConversionService conversionService () {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new DateToLocalDateTimeConverter());
        service.addConverter(new DateToLocalStringConverter());
        return service;
    }


    /**
     * 注册自定义转化类,将 Date 类型转换为 LocalDateTime
     */
    public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        @Override
        public LocalDateTime convert(Date source) {
            return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    public class DateToLocalStringConverter implements Converter<Date, String> {

        @Override
        public String convert(Date source) {
            return "121212";
        }
    }

}
