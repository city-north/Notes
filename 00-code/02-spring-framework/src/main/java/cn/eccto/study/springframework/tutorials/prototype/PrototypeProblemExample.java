package cn.eccto.study.springframework.tutorials.prototype;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 将一个 SCOPE_PROTOTYPE 类型的 bean {@link MyPrototypeBean} 多次注入到单例 {@link MySingletonBean}
 * 根据输出结果我们得出结论,{@link MySingletonBean} 引用的 {@link MyPrototypeBean} 是一个对象
 *
 * @author EricChen 2019/11/15 20:14
 */
@Configuration
public class PrototypeProblemExample {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrototypeProblemExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        System.out.println(bean.getPrototypeBean());
        Thread.sleep(1000);

        MySingletonBean bean2 = context.getBean(MySingletonBean.class);
        System.out.println(bean2.getPrototypeBean());
        System.out.println("bean 中的 MyPrototypeBean 是同一个对象吗" + bean.getPrototypeBean().equals(bean2.getPrototypeBean()));
        bean.showMessage();
    }
}
