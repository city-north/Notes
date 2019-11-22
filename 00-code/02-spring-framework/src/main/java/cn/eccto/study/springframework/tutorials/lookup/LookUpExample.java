package cn.eccto.study.springframework.tutorials.lookup;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
 * 使用 {@link org.springframework.beans.factory.annotation.Lookup} 注解完成在一个 Singleton bean 中
 * 获取一个 Prototype 类型的 bean
 *
 * @author EricChen2019/11/16 20:00
 */
@Configuration
@ComponentScan(basePackageClasses = MySingletonBean.class)
public class LookUpExample {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(LookUpExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }
}
