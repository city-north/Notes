package cn.eccto.study.eureka.consumer.feign.fallback;

import cn.eccto.study.eureka.consumer.feign.FeignClientFallbackCause;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author EricChen 2019/12/26 22:13
 */
@Component
public class FeignClientFallbackCauseFactory implements FallbackFactory<FeignClientFallbackCause> {
    @Override
    public FeignClientFallbackCause create(Throwable cause) {
        return new FeignClientFallbackCause() {
            @Override
            public String iFailSometimes() {
                return "fallback; reason was: " + cause.getMessage();
            }
        };
    }
}