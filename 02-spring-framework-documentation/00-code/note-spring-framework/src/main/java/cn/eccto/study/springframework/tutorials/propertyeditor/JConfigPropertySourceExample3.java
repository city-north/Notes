package cn.eccto.study.springframework.tutorials.propertyeditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * JavaConfig 配置方式, 自定义一个 Custom Editor 一次进行使用
 * 如果我们不想使用 Spring custom PropertyEditor 作为一个全局的类型转换类型,我们可以在运行时一个 custom Editor 实例.这样我们就可在一个指定的注入点使用一次
 *
 * @author EricChen 2019/11/21 20:10
 * @see Config#getPrice 注册了一个自定义的 {@link CustomNumberEditor}
 */
public class JConfigPropertySourceExample3 {

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

        @Bean
        @Qualifier("tradePrice")
        public Double getPrice(Environment env) {
            NumberFormat numberFormat = new DecimalFormat("##,###.00");

            CustomNumberEditor customNumberEditor = new CustomNumberEditor(Double.class, numberFormat, true);
            customNumberEditor.setAsText(env.getProperty("thePrice"));
            return (Double) customNumberEditor.getValue();
        }
    }

    public static class ClientBean {
        @Autowired
        @Qualifier("tradePrice")
        private Double price;


        public void doSomething() {
            System.out.printf("The price from prop file is %f%n", price);
        }
    }
}