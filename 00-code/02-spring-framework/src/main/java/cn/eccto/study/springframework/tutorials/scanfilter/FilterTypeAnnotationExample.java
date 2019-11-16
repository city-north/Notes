package cn.eccto.study.springframework.tutorials.scanfilter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 指定不使用标准的 filter ,并引入自定义注解 {@link MyAnnotation}
 *
 * @author EricChen 2019/11/14 16:07
 */
@Configuration
@ComponentScan(useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyAnnotation.class)})
public class FilterTypeAnnotationExample {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(FilterTypeAnnotationExample.class);
        Util.printBeanNames(context);
    }
}