package cn.eccto.study.sb.formatter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description
 *
 * @author EricChen 2019/12/09 22:22
 */
@Controller
@RequestMapping("/")
public class TradeController {
    @GetMapping("/trade")
    @ResponseBody
    public String handleRequest(@RequestParam TradeAmount amount) {
        return "Request param: " + amount;
    }
}
