package cn.eccto.study.springframework.tutorials.objectprovider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author JonathanChen 2019/11/15 20:19
 */
@Configuration
public class ObjectProviderJava8Example {


    @Bean
    MsgBean exampleBean() {
        return new MsgBean("test msg");
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ObjectProviderJava8Example.class);
//        getIfAvailable(context);
        ifAvaliable(context);

    }

    private static void ifAvaliable(AnnotationConfigApplicationContext context) {
        ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
        beanProvider.ifAvailable(MsgBean::showMessage);
    }

    private static void getIfAvailable(AnnotationConfigApplicationContext context) {
        ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
        //如果不存在就会返回默认
        MsgBean exampleBean = beanProvider.getIfAvailable(() -> new MsgBean("default msg"));
        exampleBean.showMessage();
    }

}
