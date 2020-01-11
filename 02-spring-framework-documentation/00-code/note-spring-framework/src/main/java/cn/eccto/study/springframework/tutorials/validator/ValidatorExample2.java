package cn.eccto.study.springframework.tutorials.validator;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Spring 校验机制实例,
 * 使用 {@link ValidationUtils#invokeValidator}方法调用一个自定义的 Validator
 *
 * @author EricChen 2019/11/21 20:53
 */
public class ValidatorExample2 {
    public static void main(String[] args) {

        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");

        Order order = new Order();
        order.setPrice(BigDecimal.ZERO);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(order, "order");
        ValidationUtils.invokeValidator(new OrderValidator(), order, result);
        result.getAllErrors().stream().
                forEach(e -> System.out.println(messageSource.getMessage(e, Locale.US)));
    }


    private static class Order {
        private LocalDate date;
        private BigDecimal price;

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public LocalDate getDate() {
            return date;
        }

        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Order{'date='" + date + "', price=" + price + '}';
        }
    }

    /**
     * 自定义家
     */
    private static class OrderValidator implements Validator {

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
}
