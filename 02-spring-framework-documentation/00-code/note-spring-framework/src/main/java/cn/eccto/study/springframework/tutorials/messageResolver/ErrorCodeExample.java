package cn.eccto.study.springframework.tutorials.messageResolver;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

/**
 * 本例展示了如何使用通过简单的自定义{@link Validator} 以及使用{@link ValidationUtils} 调用自定义校验器的方式
 *
 * @author EricChen 2019/11/22 21:19
 */
@Configuration
public class ErrorCodeExample {

    @Bean
    OrderValidator orderValidator() {
        return new OrderValidator();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        return messageSource;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ErrorCodeExample.class);
        runWithDefaultFormat(context);
        System.out.println("-------");
        runWithPostfixFormat(context);
        System.out.println("-------");
        runMessageCodesResolver(context);

    }

    /**
     * 使用默认格式 ({@link DefaultMessageCodesResolver.Format#PREFIX_ERROR_CODE}) 拼接用户的 error Code(拼接在前方)
     */
    public static void runWithDefaultFormat(AnnotationConfigApplicationContext context) {
        Order order = new Order();
        Validator bean = context.getBean(Validator.class);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(order, Order.class.getName());
        ValidationUtils.invokeValidator(bean, order, result);
        result.getAllErrors().stream().forEach(
                objectError ->
                        System.out.println(Arrays.toString(objectError.getCodes()))
        );
    }

    /**
     * 使用 {@link DefaultMessageCodesResolver.Format#POSTFIX_ERROR_CODE} 模式拼接全类名以及客户端自定义的 CODE
     */
    public static void runWithPostfixFormat(AnnotationConfigApplicationContext context) {
        Order order = new Order();
        order.setOrderPrice(BigDecimal.ZERO);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(order, Order.class.getName());
        DefaultMessageCodesResolver messageCodesResolver = (DefaultMessageCodesResolver) bindingResult.getMessageCodesResolver();
        messageCodesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
        OrderValidator validator = context.getBean(OrderValidator.class);
        ValidationUtils.invokeValidator(validator, order, bindingResult);
        bindingResult.getAllErrors().forEach(e -> System.out.println(Arrays.toString(e.getCodes())));
    }


    /**
     * 使用 上线文对象获取Messaage ,因为 {@link AnnotationConfigApplicationContext} 实现了 {@link MessageSource}, 需要注册 MessageSource 实例即可完成 code 与 message的转换
     */
    public static void runMessageCodesResolver(AnnotationConfigApplicationContext context) {
        Order order = new Order();
        order.setOrderPrice(BigDecimal.ZERO);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(order, Order.class.getName());
        DefaultMessageCodesResolver messageCodesResolver = (DefaultMessageCodesResolver) bindingResult.getMessageCodesResolver();
        messageCodesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
        OrderValidator validator = context.getBean(OrderValidator.class);
        ValidationUtils.invokeValidator(validator, order, bindingResult);
        bindingResult.getAllErrors().forEach(e -> System.out.println(context.getMessage(e, Locale.SIMPLIFIED_CHINESE)));
    }

    /**
     * 测试实体类
     */
    private static class Order {
        private LocalDate orderDate;
        private BigDecimal orderPrice;

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
        }

        public BigDecimal getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(BigDecimal orderPrice) {
            this.orderPrice = orderPrice;
        }
    }

    class OrderValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return clazz.equals(Order.class);
        }

        @Override
        public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors, "orderDate", "error.date.empty", "Date cannot be empty");
            ValidationUtils.rejectIfEmpty(errors, "orderPrice", "error.price.empty", "Price cannot be empty");
            Order order = (Order) target;
            if (order.getOrderPrice() != null && order.getOrderPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("orderPrice", "price.invalid", "Price must be greater than 0");
            }
        }
    }

}
