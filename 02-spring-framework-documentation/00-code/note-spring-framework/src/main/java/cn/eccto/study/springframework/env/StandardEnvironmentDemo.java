package cn.eccto.study.springframework.env;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * 代码实例 for {@link org.springframework.core.env.StandardEnvironment}
 * </p>
 *
 * @author EricChen 2021/01/14 09:48
 */
@Configuration
public class StandardEnvironmentDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(StandardEnvironmentDemo.class);

        final ConfigurableEnvironment environment = context.getEnvironment();
        final Map<String, Object> systemEnvironment = environment.getSystemEnvironment();
        printlnMap(" environment.getSystemEnvironment();", systemEnvironment);
        final Map<String, String> getenv = System.getenv();
        printlnMap("System.getenv()", getenv);

        final Properties properties = System.getProperties();
        printlnMap("System.getProperties();", properties);
        System.out.println(environment);
        final Map<String, Object> systemProperties = environment.getSystemProperties();
        printlnMap("environment.getSystemProperties();", systemProperties);
        final MutablePropertySources propertySources = environment.getPropertySources();
        System.out.println(propertySources);

        context.refresh();

        context.close();
    }


    private static void printlnMap(String name, Map map) {
//        System.out.println("-----------------------map:" + name + "-----------------------");
//        map.forEach((k, v) -> {
//            System.out.println(k + ":" + v);
//        });
//        System.out.println("-----------------------finish------------------------");
    }

}
