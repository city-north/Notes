package cn.eccto.study.springframework.tutorials.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 实例: 简单使用 Java Util Logging 打印 ,配置文件地址 classpath:logging.properties
 *
 * @author JonathanChen 2019/11/29 22:36
 */
public class JULPropertiesExample {
    public static void main(String[] args) throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:logging.properties");
        System.setProperty("java.util.logging.config.file", file.getPath());

        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(JULPropertiesExample.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }


    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static class MyBean {
        private static Log log = LogFactory.getLog(MyBean.class);

        public void doSomething() {
            log.info("doing something");
        }
    }
}
