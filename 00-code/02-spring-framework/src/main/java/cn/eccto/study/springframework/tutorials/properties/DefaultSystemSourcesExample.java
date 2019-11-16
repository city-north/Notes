package cn.eccto.study.springframework.tutorials.properties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * description
 *
 * @author EricChen 2019/11/16 21:12
 */
public class DefaultSystemSourcesExample {
    public static void main(String[] args) throws Exception{
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        ConfigurableEnvironment env = context.getEnvironment();
        printSources(env);
        System.out.println("---- System properties -----");
        printMap(env.getSystemProperties());
        System.out.println("---- System Env properties -----");
        printMap(env.getSystemEnvironment());
    }

    private static void printSources(ConfigurableEnvironment env) {

        System.out.println("---- property sources ----");
        env.getPropertySources().forEach(propertySource -> System.out.println("name =  " + propertySource.getName() + "\nsource = " + propertySource
                .getSource().getClass() + "\n"));
    }

    private static void printMap(Map<?, ?> map) {
        map.entrySet()
                .stream()
                .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));

    }
}
