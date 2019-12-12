package cn.eccto.study.sb.propertiesvalidate;

import cn.eccto.study.sb.customconverter.MyAppProperties;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 如何使用`@Valid`注解校验嵌套(级联)的配置属性
 *
 * @author EricChen 2019/12/12 10:47
 */
@SpringBootApplication
public class PropertyValidateExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(PropertyValidateExample.class)
                .profiles("property-validate")
                .web(WebApplicationType.NONE)
                .run();
        MyAppProperties bean = context.getBean(MyAppProperties.class);
        System.out.println(bean);
    }

}
