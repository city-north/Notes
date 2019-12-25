package cn.eccto.study.eureka.consumer.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 自动配置类
 *
 * @author EricChen 2019/12/25 15:08
 */
@Configuration
@EnableFeignClients("cn.eccto.study.eureka.consumer")
public class FeignAutoConfiguration {

}
