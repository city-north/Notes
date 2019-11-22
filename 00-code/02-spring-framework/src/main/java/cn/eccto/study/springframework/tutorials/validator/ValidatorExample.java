package cn.eccto.study.springframework.tutorials.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

/**
 * Spring 校验机制实例,
 * 1. 准备客户端 {@link ClientBean}, 实例 bean {@link Order}
 * 2. 自定义 {@link OrderValidator} 实现 Spring 的接口 {@link Validator}
 * 3. 在客户端 {@link ClientBean} 中写校验方法 {@link ClientBean#validateOrder},通过messageSource 获取配置文件中配置的错误信息
 * @author EricChen 2019/11/21 20:53
 */
public class ValidatorExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        Config.class);

        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

    @Configuration
    public static class Config {

        @Bean
        public ClientBean clientBean() {
            return new ClientBean();
        }

        @Bean
        public Order order() {
            //in real scenario this order should be coming from user interface
            Order order = new Order();
            order.setPrice(BigDecimal.ZERO);
            return order;
        }
    }

    private static class ClientBean {
        @Autowired
        private Order order;

        private void processOrder() {
            if (validateOrder()) {
                System.out.println("processing " + order);
            }
        }

        private boolean validateOrder() {
            DataBinder dataBinder = new DataBinder(order);
            dataBinder.addValidators(new OrderValidator());
            dataBinder.validate();

            if (dataBinder.getBindingResult().hasErrors()) {
                ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
                messageSource.setBasename("messages/messages");
                System.out.println(messageSource.getMessage("order.invalid", null, Locale.US));
                dataBinder.getBindingResult().getAllErrors()
                        .stream()
                        .forEach(e -> System.out.println(messageSource.getMessage(e, Locale.US)));
                return false;
            }
            return true;
        }
    }

    private static class Order {
        private Date date;
        private BigDecimal price;

        public void setDate(Date date) {
            this.date = date;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Date getDate() {
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
     * 自定义 {@link Validator}  ,通过使用 {@link ValidationUtils} 内的方法,
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
            if (order.getPrice() != null && order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("price", "price.invalid");
            }
        }
    }
}