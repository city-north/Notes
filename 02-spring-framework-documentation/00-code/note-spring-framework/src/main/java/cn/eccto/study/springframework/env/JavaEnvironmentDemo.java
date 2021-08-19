package cn.eccto.study.springframework.env;

import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author JonathanChen 2021/01/14 10:57
 */
public class JavaEnvironmentDemo {

    public static void main(String[] args) {
        final Map<String, String> getenv = System.getenv();
        printlnMap("System.getenv();", System.getenv());
        final Properties properties = System.getProperties();
        printlnMap("System.getProperties();", properties);
        final String property = System.getProperty("company");
        System.out.println(property);
    }

    private static void printlnMap(String name, Map map) {
        System.out.println("-----------------------map:" + name + "-----------------------");
        map.forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });
        System.out.println("-----------------------finish------------------------");
    }
}
