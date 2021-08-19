package cn.eccto.study.springframework.tutorials.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link ReloadableResourceBundleMessageSource} 测试类,通过反复读取配置文件中的配置,动态修改配置文件中的数据,会输出
 *
 * @author JonathanChen 2019/11/18 20:42
 */
@Configuration
public class ReloadableMessageSourceExample {

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        //uncomment next line to change the locale
        //   Locale.setDefault(Locale.FRANCE);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ReloadableMessageSourceExample.class);
        context.refresh();

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheMillis(500);
        return messageSource;
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            //we are repeating 20 times with 2sec sleep, so that we can modify the
            //files outside (in target/classes folder) to watch the change reloading.
            for (int i = 0; i < 20; i++) {
                System.out.println(messageSource.getMessage("app.name",
                        new Object[]{"Joe"},
                        Locale.getDefault()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
