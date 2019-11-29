package cn.eccto.study.springframework.tutorials.scanfilter;

import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * description
 *
 * @author EricChen 2019/11/14 20:09
 */
public class Util {
    public static void printBeanNames(ApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.stream(beanNames)
                .filter(n -> !n.contains("springframework"))
                .forEach(System.out::println);
    }
}