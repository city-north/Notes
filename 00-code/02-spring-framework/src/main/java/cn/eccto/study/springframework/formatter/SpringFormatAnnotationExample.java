package cn.eccto.study.springframework.formatter;

import cn.eccto.study.springframework.formatter.info.Order;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.DataBinder;

/**
 * Spring 基于注解的格式化
 * <p>
 * 1. 自定义测试类{@link Order},设置注解 NumberFormat 和 DateTimeFormat
 *
 * @author EricChen 2019/11/04 15:29
 */
class SpringFormatAnnotationExample {


    public static void main(String[] args) {
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
        conversionService.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
        Order order = new Order();
        DataBinder dataBinder = new DataBinder(order);
        dataBinder.setConversionService(conversionService);
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("price", "2.7%");
        mpv.add("date", "2016");

        dataBinder.bind(mpv);
        dataBinder.getBindingResult()
                .getModel()
                .entrySet()
                .forEach(System.out::println);
        System.out.println("设置后的 date 字段" + order.getDate());
        System.out.println("设置后的 price 字段" + order.getPrice());
    }

}








