package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.scanfilter.MyBean1;
import cn.eccto.study.springframework.tutorials.scanfilter.MyBean3;
import cn.eccto.study.springframework.tutorials.scanfilter.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 指定不使用标准的 filter ,指定需要引入的类 {@link MyBean1}和{@link MyBean3}
 *
 * @author EricChen 2019/11/14 16:16
 */
@Configuration
@ComponentScan(useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = {MyBean1.class, MyBean3.class})})
public class FilterTypeAssignableExample {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(FilterTypeAssignableExample.class);
        Util.printBeanNames(context);
    }
}