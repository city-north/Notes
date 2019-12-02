package cn.eccto.study.nonesb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * description
 *
 * @author EricChen 2019/12/02 21:29
 */
@Controller
public class KickStartController {


    @RequestMapping("/")
    @ResponseBody
    public String helloHandler() {
        return "<h1>Hello World!</h1>";
    }


    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message","EricChen");
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping("/index2")
    public String index2() {
        return "index";
    }
}
