package cn.eccto.study.springframework.tutorials.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 示例: 使用 @Import 注解导入 Selector 选择器
 *
 * @author EricChen 2019/11/27 22:10
 */
public class ImportSelectorExample {
    public static void main (String[] args) {
        System.setProperty("myProp", "1");
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @Import(MyImportSelector.class)
    public static class MainConfig {

        @Bean
        ClientBean clientBean () {
            return new ClientBean();
        }

    }

    public static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething () {
            System.out.println(appBean.getMessage());
        }

    }


    public static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports (AnnotationMetadata importingClassMetadata) {
            String prop = System.getProperty("myProp");
            if ("1".equals(prop)) {
                return new String[]{MyConfig1.class.getName()};
            } else {
                return new String[]{MyConfig2.class.getName()};
            }
        }
    }

    public static class AppBean {
        private String message;

        public AppBean (String message) {
            this.message = message;
        }

        public String getMessage () {
            return message;
        }
    }

    @Configuration
    public static class MyConfig1 {
        @Bean
        AppBean appBean () {
            return new AppBean("from config 1");
        }
    }

    @Configuration
    public static class MyConfig2 {
        @Bean
        AppBean appBean () {
            return new AppBean("from config 2");
        }
    }
}
