package cn.eccto.study.sb.customconverter;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * description
 *
 * @author EricChen 2019/12/12 10:22
 */
@SpringBootApplication
public class CustomConverterExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CustomConverterExample.class)
                .profiles("custom-converter")
                .web(WebApplicationType.NONE)
                .run();
        MyAppProperties bean = context.getBean(MyAppProperties.class);
        System.out.println(bean);
    }

}
