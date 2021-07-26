package cn.eccto.study.springframework.validation.spr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class Config {

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
