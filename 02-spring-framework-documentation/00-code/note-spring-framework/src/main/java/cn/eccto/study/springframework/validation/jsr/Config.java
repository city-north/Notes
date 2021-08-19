package cn.eccto.study.springframework.validation.jsr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import java.util.Date;

/**
 * JSR 标准注解的数据的数据校验
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link cn.eccto.study.springframework.validation.spr.ClientBean}
 * 3. 配置配置类 {@link cn.eccto.study.springframework.validation.spr.Config}
 *
 * @author JonathanChen 2019/11/04 20:09
 */
@Configuration
public class Config {

    @Bean
    public ClientBean clientBean() {
        return new ClientBean();
    }

    @Bean
    public Order order() {
        Order order = new Order();
        //order.setPrice(BigDecimal.TEN);
        order.setDate(new Date(System.currentTimeMillis() - 60000));
        return order;
    }

    @Bean
    public Validator validatorFactory() {
        return new LocalValidatorFactoryBean();
    }
}
