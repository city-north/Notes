package cn.eccto.study.sb.registservlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description
 *
 * @author EricChen 2019/12/09 21:45
 */
@Controller
public class MyController {

    @RequestMapping("/*")
    @ResponseBody
    public String handler() {
        return "response form spring controller method";
    }
}
