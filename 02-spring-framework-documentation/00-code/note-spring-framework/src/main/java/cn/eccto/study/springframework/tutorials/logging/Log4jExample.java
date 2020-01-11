package cn.eccto.study.springframework.tutorials.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 代码实例 log4j 使用
 *
 * @author EricChen 2019/11/29 23:32
 */
@Configuration
public class Log4jExample {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(Log4jExample.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    public static class MyBean {
        private static Log log = LogFactory.getLog(MyBean.class);

        public void doSomething() {
            log.info("doing something");
        }
    }
}
