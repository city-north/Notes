package cn.eccto.study.eureka.consumer.feign.fallback;

import cn.eccto.study.eureka.consumer.feign.ConsumerFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author EricChen 2019/12/25 15:35
 */
@Component
public class ConsumerFeignFallback implements ConsumerFeign {
    public static final Logger logger = LoggerFactory.getLogger(ConsumerFeignFallback.class);

    @Override
    public String helloWorld() {
        logger.error("call helloWorld failed");
        throw new RuntimeException("call helloWorld failed");
    }

    @Override
    public String index() {
        logger.error("call index failed");
        throw new RuntimeException("call index failed");
    }
}
