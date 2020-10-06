package cn.eccto.study.springframework.tutorials.message;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * {@link MessageSourceAware} 代码实例
 *
 * @author EricChen 2019/11/18 20:31
 */
@Configuration
public class MessageSourceAwareExample {
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

    public static class MyBean implements MessageSourceAware {
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[]{"Joe"},Locale.SIMPLIFIED_CHINESE));
        }

        @Override
        public void setMessageSource(MessageSource messageSource) {
            this.messageSource = messageSource;

        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MessageSourceAwareExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }
}
