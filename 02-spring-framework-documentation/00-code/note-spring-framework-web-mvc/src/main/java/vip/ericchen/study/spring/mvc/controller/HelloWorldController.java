package vip.ericchen.study.spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  a common controller in spring mvc
 *
 * @author EricChen 2020/01/11 17:40
 */
@Controller
public class HelloWorldController {


    @RequestMapping("/")
    @ResponseBody
    public String helloWorld(String name) {
        return "hello ," + name;
    }
}
