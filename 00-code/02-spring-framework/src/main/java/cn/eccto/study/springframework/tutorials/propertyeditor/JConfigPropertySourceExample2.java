package cn.eccto.study.springframework.tutorials.propertyeditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本例介绍了, 不适用 XML 注册方式,转而使用 Spring 的 {@link PropertyEditorRegistrar} 去注册一个 custom PropertyEditor 使用 JavaConfig的方式进行注
 *
 * @author EricChen 2019/11/21 20:07
 * @see JConfigPropertySourceExample2.MyCustomBeanRegistrar#registerCustomEditors 自定义注册方式
 * @see XmlSpringCustomEditorExample 使用 xml 方式进行注册
 */
public class JConfigPropertySourceExample2 {

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
        public static CustomEditorConfigurer customEditorConfigurer() {
            CustomEditorConfigurer cec = new CustomEditorConfigurer();
            cec.setPropertyEditorRegistrars(
                    new PropertyEditorRegistrar[]{
                            new MyCustomBeanRegistrar()});
            return cec;
        }
    }

    public static class ClientBean {
        @Value("${theTradeDate}")
        private Date tradeDate;

        public void doSomething() {
            System.out.printf("The trade date from prop file is %s%n", tradeDate);
        }
    }

    /**
     * 通过 Spring 提供的 Custom 类自定义解析方式
     */
    public static class MyCustomBeanRegistrar implements PropertyEditorRegistrar {
        @Override
        public void registerCustomEditors(PropertyEditorRegistry registry) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setLenient(false);
            registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormatter, true));
        }
    }
}
