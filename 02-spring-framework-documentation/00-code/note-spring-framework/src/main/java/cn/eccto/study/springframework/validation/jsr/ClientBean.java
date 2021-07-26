package cn.eccto.study.springframework.validation.jsr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

/**
 * JSR 标准注解的数据的数据校验
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link cn.eccto.study.springframework.validation.spr.ClientBean}
 * 3. 配置配置类 {@link cn.eccto.study.springframework.validation.spr.Config}
 *
 * @author JonathanChen 2019/11/04 20:09
 */
public class ClientBean {
    @Autowired
    private Order order;

    @Autowired
    Validator validator;

    public void processOrder() {
        if (validateOrder()) {
            System.out.println("processing " + order);
        }
    }

    private boolean validateOrder() {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        Set<ConstraintViolation<Order>> c = validator.validate(order);
        if (c.size() > 0) {
            ResourceBundleMessageSource messageSource =
                    new ResourceBundleMessageSource();
            //使用 messageSource
            messageSource.setBasename("messages/messages");
            messageSource.setDefaultEncoding("UTF-8");
            System.out.println("Order validation errors:");
            c.stream().map(v -> messageSource.getMessage(v.getMessage(), null, Locale.SIMPLIFIED_CHINESE))
                    .forEach(System.out::println);
            return false;
        }
        return true;
    }


}
