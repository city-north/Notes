package vip.ericchen.ecrpc.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 22:58
 */
public class SpringApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        RpcServer bean = applicationContext.getBean(RpcServer.class);
        bean.publish();
    }
}
