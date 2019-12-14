package cn.eccto.study.eureka.consumer.service.impl;

import cn.eccto.study.eureka.consumer.service.IEurekaConsumerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static cn.eccto.study.common.utils.EcctoService.ECCTO_PROVIDER_PRIFIX;

/**
 * description
 *
 * @author EricChen 2019/12/13 16:18
 */
@Service
public class EurekaConsumerServiceImpl implements IEurekaConsumerService {

    @Autowired
    private RestTemplate restTemplate;


    @HystrixCommand(fallbackMethod = "helloFallback")
    public String helloService() {
        return restTemplate.getForEntity(ECCTO_PROVIDER_PRIFIX + "/hello", String.class).getBody();
    }

    public String helloFallback() {
        return "error message from helloFallback";
    }

}
