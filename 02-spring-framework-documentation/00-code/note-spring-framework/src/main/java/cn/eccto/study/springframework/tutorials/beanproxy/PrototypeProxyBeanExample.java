package cn.eccto.study.springframework.tutorials.beanproxy;

import cn.eccto.study.springframework.formatter.configuration.AppConfig;
import cn.eccto.study.springframework.tutorials.prototype.MyPrototypeBean;
import cn.eccto.study.springframework.tutorials.prototype.MySingletonBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
 * description
 *
 * @author JonathanChen 2019/11/16 20:20
 */
@Configuration
public class PrototypeProxyBeanExample {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrototypeProxyBeanExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }

}
