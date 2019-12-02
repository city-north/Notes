package cn.eccto.study.sb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 *
 * @author EricChen 2019/12/02 21:29
 */
@RestController
public class KickStartController {


    @RequestMapping("/")
    public String helloHandler() {
        return "<h1>Hello World!</h1>";
    }

}
