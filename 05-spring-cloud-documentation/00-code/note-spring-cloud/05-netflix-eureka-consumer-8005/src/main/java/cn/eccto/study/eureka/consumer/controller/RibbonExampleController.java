package cn.eccto.study.eureka.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static cn.eccto.study.common.utils.EcctoService.ECCTO_PROVIDER_PRIFIX;

/**
 * 02-netflix-eureka-client-8002 和  04-netflix-eureka-client-8004 都注册到了 Eureka
 * <p>
 * 通过使用     @LoadBalanced 注解完成客户端负载均衡的实例
 *
 * @author EricChen 2019/12/13 14:43
 */
@RestController
@RequestMapping("/ribbon")
public class RibbonExampleController {


    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        return restTemplate.getForObject(ECCTO_PROVIDER_PRIFIX + "/hello", String.class);
    }


}
