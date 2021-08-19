package cn.eccto.study.springframework.tutorials.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.IOException;
import java.util.Locale;

/**
 * 测试{@link ResourceBundleMessageSource}
 *
 * @author JonathanChen 2019/11/18 20:39
 */
@Configuration
public class MessageSourceExample {

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        //uncomment next line to change the locale
        //Locale.setDefault(Locale.FRANCE);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MessageSourceExample.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }


    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[]{"Joe"},
                    Locale.getDefault()));
        }
    }
}
