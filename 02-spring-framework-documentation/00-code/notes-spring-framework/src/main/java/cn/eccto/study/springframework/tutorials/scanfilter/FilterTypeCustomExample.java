package cn.eccto.study.springframework.tutorials.scanfilter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 指定不使用标准的 filter ,并引入自定义注解扫描 {@link MyTypeFilter}
 *
 * @author EricChen 2019/11/14 20:49
 */
@Configuration
@ComponentScan(useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = MyTypeFilter.class)
)
public class FilterTypeCustomExample {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(FilterTypeCustomExample.class);
        Util.printBeanNames(context);
    }
}