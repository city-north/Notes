package cn.eccto.study.springframework.tutorials.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 代码实例 log4j2 使用
 *
 * @author JonathanChen 2019/11/29 23:32
 */
@Configuration
public class Log4j2Example {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(Log4j2Example.class);

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
