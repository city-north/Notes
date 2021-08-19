package cn.eccto.study.springframework.tutorials.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 示例: 使用自定义注解的方式,导入选择器
 * 自定义注解上要表名@Import 注解
 *
 * @author JonathanChen 2019/11/27 22:17
 */
public class ImportSelectorWithAnnotationExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @EnableSomeModule("someValue")
    public static class MainConfig {
        @Bean
        ClientBean clientBean() {
            return new ClientBean();
        }
    }

    public static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething() {
            System.out.println(appBean.getMessage());
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Import(MyImportSelector.class)
    public @interface EnableSomeModule {
        String value() default "";
    }


    public static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableSomeModule.class.getName(), false));
            String value = attributes.getString("value");
            if ("someValue".equals(value)) {
                return new String[]{MyConfig1.class.getName()};
            } else {
                return new String[]{MyConfig2.class.getName()};
            }
        }
    }

    public static class AppBean {
        private String message;

        public AppBean(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Configuration
    public static class MyConfig1 {
        @Bean
        AppBean appBean() {
            return new AppBean("from config 1");
        }
    }

    @Configuration
    public static class MyConfig2 {
        @Bean
        AppBean appBean() {
            return new AppBean("from config 2");
        }
    }
}
