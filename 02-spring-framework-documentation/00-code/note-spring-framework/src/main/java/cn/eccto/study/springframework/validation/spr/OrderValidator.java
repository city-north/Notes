package cn.eccto.study.springframework.validation.spr;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Spring 自定义校验器实例
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link ClientBean}
 * 3. 配置校验器 {@link OrderValidator}
 * 4. 配置配置类 {@link Config}
 *
 * @author JonathanChen 2019/11/04 19:29
 */
public class OrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "date", "date.empty");
        ValidationUtils.rejectIfEmpty(errors, "price", "price.empty");
        Order order = (Order) target;
        if (order.getPrice() != null &&
                order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "price.invalid");
        }
    }
}