package cn.eccto.study.springframework.validation;

import cn.eccto.study.springframework.validation.spr.ClientBean;
import cn.eccto.study.springframework.validation.spr.Config;
import cn.eccto.study.springframework.validation.spr.Order;
import cn.eccto.study.springframework.validation.spr.OrderValidator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring 自定义校验器实例
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link ClientBean}
 * 3. 配置校验器 {@link OrderValidator}
 * 4. 配置配置类 {@link Config}
 *
 * @author EricChen 2019/11/04 19:29
 */
public class ValidatorExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

}
