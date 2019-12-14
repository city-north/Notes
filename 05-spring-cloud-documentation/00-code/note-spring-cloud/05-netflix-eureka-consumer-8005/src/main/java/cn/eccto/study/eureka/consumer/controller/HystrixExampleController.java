package cn.eccto.study.eureka.consumer.controller;

import cn.eccto.study.eureka.consumer.service.IEurekaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 *
 * @author EricChen 2019/12/13 16:21
 */
@RestController
@RequestMapping("hystrix")
public class HystrixExampleController {

    @Autowired
    private IEurekaConsumerService eurekaConsumerService;

    @GetMapping("/hello")
    public String hello() {
        return eurekaConsumerService.helloService();
    }

}
