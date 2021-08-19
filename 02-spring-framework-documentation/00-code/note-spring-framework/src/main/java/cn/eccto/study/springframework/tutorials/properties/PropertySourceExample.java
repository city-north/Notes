package cn.eccto.study.springframework.tutorials.properties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * {@link @PropertySource} 注解提供了便利的机制让我们能添加属性源(properties source)到我们环境中
 * (Environment)中
 *
 * @author JonathanChen 2019/11/16 21:20
 */
@Configuration
@PropertySource("classpath:tutorials/properties/app.properties")
public class PropertySourceExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PropertySourceExample.class);
        ConfigurableEnvironment env = context.getEnvironment();
        String property = env.getProperty("some-strProp");
        System.out.println("some-strProp value is " + property);

        //printing all sources
        System.out.println(env.getPropertySources());
    }
}
