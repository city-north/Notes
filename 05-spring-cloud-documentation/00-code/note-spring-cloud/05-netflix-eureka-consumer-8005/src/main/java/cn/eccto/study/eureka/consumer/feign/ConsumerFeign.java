package cn.eccto.study.eureka.consumer.feign;

import cn.eccto.study.eureka.consumer.feign.fallback.ConsumerFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * description
 *
 * @author EricChen 2019/12/25 15:35
 */
@FeignClient(value = "eccto-provider", fallback = ConsumerFeignFallback.class)
public interface ConsumerFeign {

    @GetMapping("/hello")
    String helloWorld();
}
