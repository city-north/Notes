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
 * 测试 {@link ResourceBundleMessageSource} 的层次结构,如果 child 拿不到相关的信息,那么就去 parent 里拿
 *
 * @author JonathanChen 2019/11/18 20:47
 */
@Configuration
public class ParentMessageSourceExample {


    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        //uncomment next line to change the locale
        //Locale.setDefault(Locale.FRANCE);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ParentMessageSourceExample.class);

        ParentMessageSourceExample.MyBean bean = context.getBean(ParentMessageSourceExample.MyBean.class);
        bean.doSomething();
    }


    @Bean
    public ParentMessageSourceExample.MyBean myBean() {
        return new ParentMessageSourceExample.MyBean();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("utf-8");
        ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
        parent.setBasename("messages/parent");
        parent.setDefaultEncoding("utf-8");
        messageSource.setParentMessageSource(parent);
        return messageSource;
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("test.parent", new Object[]{"Joe"},
                    Locale.getDefault()));
            System.out.println(messageSource.getMessage("test.child", new Object[]{"Joe"},
                    Locale.getDefault()));
        }
    }
}
