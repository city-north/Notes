package cn.eccto.study.sb.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * description
 *
 * @author EricChen 2019/12/09 21:59
 */
@Controller
public class MyController {
    @GetMapping("/")
    public String handle(Model model) {
        model.addAttribute("msg", "test msg from controller");
        return "myCustomView";
    }
}
