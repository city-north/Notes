package cn.eccto.study.springframework.tutorials.beanproxy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
 * 使用 JDK 代理技术
 *
 * @author EricChen 2019/11/16 16:20
 */
@Configuration
public class PrototypeJDKProxyBeanExample {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.INTERFACES)
    public MyPrototypeBean prototypeBean () {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean () {
        return new MySingletonBean();
    }


    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrototypeJDKProxyBeanExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }

}
