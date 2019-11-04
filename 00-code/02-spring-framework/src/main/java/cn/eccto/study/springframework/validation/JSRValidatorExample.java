package cn.eccto.study.springframework.validation;

import cn.eccto.study.springframework.validation.jsr.ClientBean;
import cn.eccto.study.springframework.validation.jsr.Config;
import cn.eccto.study.springframework.validation.jsr.Order;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * JSR 标准注解的数据的数据校验
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link cn.eccto.study.springframework.validation.spr.ClientBean}
 * 3. 配置配置类 {@link cn.eccto.study.springframework.validation.spr.Config}
 *
 * @author EricChen 2019/11/04 20:09
 */
public class JSRValidatorExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

}
