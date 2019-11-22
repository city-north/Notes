package cn.eccto.study.springframework.tutorials.implicit;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/11/14 20:16
 */
@Configuration
@ComponentScan({"cn.eccto.study.springframework.tutorials.implicit"})
public class ImplicitExample {

    public static void main (String... strings) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ImplicitExample.class);
        OrderServiceClient bean = context.getBean(OrderServiceClient.class);
        bean.showPendingOrderDetails();
    }


}