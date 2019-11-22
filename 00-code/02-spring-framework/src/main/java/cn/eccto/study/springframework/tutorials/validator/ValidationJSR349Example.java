package cn.eccto.study.springframework.tutorials.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * 基于 JSR-349 注解方式进行校验
 *
 * @author EricChen 2019/11/21 20:34
 */
public class ValidationJSR349Example {

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

            Order order = new Order();
            // order.setPrice(BigDecimal.TEN);
            order.setDate(new Date(System.currentTimeMillis() - 60000));
            return order;
        }

        @Bean
        public Validator validatorFactory() {
            return new LocalValidatorFactoryBean();
        }
    }

    private static class ClientBean {
        @Autowired
        private Order order;
        @Autowired
        Validator validator;

        private void processOrder() {
            if (validateOrder()) {
                System.out.println("processing " + order);
            }
        }

        private boolean validateOrder() {
            Locale.setDefault(Locale.US);
            Set<ConstraintViolation<Order>> c = validator.validate(order);
            if (c.size() > 0) {
                System.out.println("Order validation errors:");
                c.stream().map(v -> v.getMessage())
                        .forEach(System.out::println);
                return false;
            }
            return true;
        }
    }

    private static class Order {
        @NotNull(message = "{date.empty}")
        @Future(message = "{date.future}")
        private Date date;

        @NotNull(message = "{price.empty}")
        @DecimalMin(value = "0", inclusive = false, message = "{price.invalid}")
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
}