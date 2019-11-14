package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.lazy.AlwaysBeingUsedBean;
import cn.eccto.study.springframework.tutorials.lazy.RarelyUsedBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * description
 *
 * @author qiang.chen04@hand-china.com 2019/11/14 12:36
 */
@Configuration
public class LazyInitExample {

    @Bean
    public AlwaysBeingUsedBean alwaysBeingUsedBean() {
        return new AlwaysBeingUsedBean();
    }

    @Bean
    @Lazy
    public RarelyUsedBean rarelyUsedBean() {
        return new RarelyUsedBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(LazyInitExample.class);
        System.out.println("Spring container started and is ready");
        RarelyUsedBean bean = context.getBean(RarelyUsedBean.class);
        bean.doSomething();
    }
}
