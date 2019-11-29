package cn.eccto.study.springframework.tutorials.propertyeditor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Currency;

/**
 * 本实例介绍了 使用 {@link Value} 注解 和{@link PropertySource} 注解注入 properties 参数值,我们不需要进行任何配置,因为 Spring 已经为我们做了这方便的转换,将 text 文本转换成 对象
 *
 * @author EricChen 2019/11/21 20:05
 */
public class JConfigPropertySourceExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @PropertySource("classpath:tutorials/propertyeditor/app.properties")
    public static class Config {
        @Bean
        public ClientBean clientBean() {
            return new ClientBean();
        }
    }

    public static class ClientBean {
        @Value("${theCurrency}")
        private Currency currency;

        public void doSomething() {
            System.out.printf("The currency from prop file is %s%n", currency);
            System.out.printf("The currency name is %s%n", currency.getDisplayName());
        }
    }
}
