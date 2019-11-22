package cn.eccto.study.springframework.tutorials.objectprovider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * description
 *
 * @author EricChen 2019/11/15 20:19
 */
@Configuration
public class ObjectProviderStreamExample {


    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    MsgBean msgBean() {
        return new MsgBean("test msg 1");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    MsgBean msgBean2() {
        return new MsgBean("test msg 2");
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ObjectProviderStreamExample.class);
        forEachRemaining(context);
        System.out.println("--------------------万恶的分割线--------------------");
        orderedStream(context);

    }

    /**
     * 测试 Stream 迭代器
     * @param context 上下文
     */
    private static void forEachRemaining(AnnotationConfigApplicationContext context) {
        ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
        beanProvider.iterator().forEachRemaining(System.out::println);
    }

    /**
     * 测试排序后的 Stream
     * @param context 上下文
     */
    private static void orderedStream(AnnotationConfigApplicationContext context) {
        ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
        System.out.println("-- default order --");
        beanProvider.stream().forEach(System.out::println);
        System.out.println("-- ordered by @Order --");
        beanProvider.orderedStream().forEach(System.out::println);
    }

}
