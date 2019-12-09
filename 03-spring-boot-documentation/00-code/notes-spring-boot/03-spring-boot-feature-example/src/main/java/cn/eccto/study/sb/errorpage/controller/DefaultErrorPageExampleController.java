package cn.eccto.study.sb.errorpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误界面示例,默认情况 application.properties 设置 `server.error.whitelabel.enabled` 可以关闭默认情况
 *
 * @author EricChen 2019/12/04 09:42
 */
@Controller
public class DefaultErrorPageExampleController {
    @RequestMapping("/")
    public String handler(Model model) {
        model.addAttribute("msg", "a spring-boot example");
        return "myPage";
    }

    @RequestMapping("/test")
    public void handler2() {
        throw new RuntimeException("exception from handler2");
    }
}
