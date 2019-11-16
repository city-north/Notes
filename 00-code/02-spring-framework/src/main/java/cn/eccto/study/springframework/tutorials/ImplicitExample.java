package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.implicit.OrderServiceClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/11/14 17:16
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