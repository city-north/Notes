package cn.eccto.study.eureka.consumer.feign;

import cn.eccto.study.eureka.consumer.feign.fallback.FeignClientFallbackCauseFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * description
 *
 * @author EricChen 2019/12/26 22:12
 */
@FeignClient(name = "eccto-config",fallbackFactory = FeignClientFallbackCauseFactory.class)
public interface FeignClientFallbackCause {
    @RequestMapping(method = RequestMethod.GET, value ="/hello")
    String iFailSometimes();
}
