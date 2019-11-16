package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.scanfilter.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * description
 *
 * @author EricChen 2019/11/14 16:25
 */
@Configuration
@ComponentScan(basePackages = "cn.eccto.study.springframework.tutorials.scanfilter"
        , useDefaultFilters = false
        , includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[12]")
)
public class FilterTypeRegexExample {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(FilterTypeRegexExample.class);
        Util.printBeanNames(context);
    }
}