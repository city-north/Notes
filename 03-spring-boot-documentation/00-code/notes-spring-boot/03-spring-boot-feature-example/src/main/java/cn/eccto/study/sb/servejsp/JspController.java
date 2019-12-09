package cn.eccto.study.sb.servejsp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 这里返回 myView 会去 src/main/resources/META-INF/resources/WEB-INF/views/myView.jsp 的文件
 *
 * @author EricChen 2019/12/06 18:56
 */
@Controller
public class JspController {

    @RequestMapping("/")
    public String handler(Model model) {
        model.addAttribute("msg", "a jar packaging example");
        return "myView";
    }
}
