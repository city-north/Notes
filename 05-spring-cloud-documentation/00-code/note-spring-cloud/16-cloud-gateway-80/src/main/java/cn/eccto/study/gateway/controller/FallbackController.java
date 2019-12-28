package cn.eccto.study.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义 hystrix_route fallback
 *
 * @author EricChen 2019/12/28 17:11
 */
@RestController
public class FallbackController {


    @GetMapping("/fallback")
    public String fallback() {
        return "fallback! from gateway";
    }

}
