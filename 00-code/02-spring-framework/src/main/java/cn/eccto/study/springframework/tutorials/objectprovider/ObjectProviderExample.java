package cn.eccto.study.springframework.tutorials.objectprovider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * description
 *
 * @author EricChen 2019/11/15 14:19
 */
@Configuration
public class ObjectProviderExample {


//    @Bean
//    ExampleBean exampleBean() {
//        return new ExampleBean();
//    }


    @Bean
    @Lazy
    ExampleBean2 exampleBean2(String arg) {
        return new ExampleBean2(arg);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ObjectProviderExample.class);
//        getBeanProviderExample(context);
//        getIfUnique(context);
//        getObject(context);
        getObjectWithArg(context);
    }


    public static void getBeanProviderExample(AnnotationConfigApplicationContext context) {
        ObjectProvider<ExampleBean> beanProvider = context.getBeanProvider(ExampleBean.class);
        ExampleBean exampleBean = beanProvider.getIfAvailable();
        System.out.println("example bean: " + exampleBean);
        if (exampleBean != null) {
            exampleBean.doSomething();
        }

    }

    private static void getIfUnique(AnnotationConfigApplicationContext context) {
        ObjectProvider<ExampleBean> beanProvider = context.getBeanProvider(ExampleBean.class);
        ExampleBean exampleBean = beanProvider.getIfUnique();
        System.out.println("example bean: " + exampleBean);
        if (exampleBean != null) {
            exampleBean.doSomething();
        }
    }

    private static void getObject(AnnotationConfigApplicationContext context) {
        ObjectProvider<ExampleBean> beanProvider = context.getBeanProvider(ExampleBean.class);
        ExampleBean exampleBean = beanProvider.getObject();
        exampleBean.doSomething();
    }

    private static void getObjectWithArg(AnnotationConfigApplicationContext context) {
        ObjectProvider<ExampleBean2> beanProvider = context.getBeanProvider(ExampleBean2.class);
        ExampleBean2 exampleBean = beanProvider.getObject("test arg");
        exampleBean.doSomething();
    }

}
