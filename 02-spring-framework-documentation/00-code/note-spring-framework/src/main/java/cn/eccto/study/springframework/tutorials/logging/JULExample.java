package cn.eccto.study.springframework.tutorials.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 实例: 简单使用 Java Util Logging 打印
 *
 * @author JonathanChen 2019/11/29 22:36
 */
public class JULExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(JULExample.class);
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
