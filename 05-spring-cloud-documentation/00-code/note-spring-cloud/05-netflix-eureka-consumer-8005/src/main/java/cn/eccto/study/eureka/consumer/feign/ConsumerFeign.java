package cn.eccto.study.eureka.consumer.feign;

import cn.eccto.study.eureka.consumer.config.ConsumerFeignConfiguration;
import cn.eccto.study.eureka.consumer.feign.fallback.ConsumerFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FeignClient 实例
 *
 * @author EricChen 2019/12/25 15:35
 */
@FeignClient(value = "eccto-provider", configuration = ConsumerFeignConfiguration.class, fallback = ConsumerFeignFallback.class)
public interface ConsumerFeign {

    @GetMapping("/hello?username=eric&password=123")
    String helloWorld();


    @GetMapping("/?username=eric&password=123")
    String index();
}
