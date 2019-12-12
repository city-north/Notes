package cn.eccto.study.sb.typesafebinding;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 使用 配置文件类 {@link MyAppProperties} 读取配置文件
 *
 * @author EricChen 2019/12/11 22:06
 */
@SpringBootApplication
public class TypeSafePropertyBindingExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TypeSafePropertyBindingExample.class)
                .profiles("type-safe")
                .run(args);
        MyAppProperties appProperties = context.getBean(MyAppProperties.class);
        MyAppNestProperties nestProperties = context.getBean(MyAppNestProperties.class);

        System.out.println(appProperties);
        System.out.println(nestProperties);
    }

}
