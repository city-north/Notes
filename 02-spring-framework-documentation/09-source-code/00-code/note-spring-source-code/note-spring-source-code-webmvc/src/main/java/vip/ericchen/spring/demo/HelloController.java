package vip.ericchen.spring.demo;

import vip.ericchen.spring.demo.service.IHelloService;
import vip.ericchen.study.spring.framework.stereotype.Autowired;
import vip.ericchen.study.spring.framework.stereotype.Controller;
import vip.ericchen.study.spring.framework.web.bind.annotation.RequestMapping;
import vip.ericchen.study.spring.framework.web.bind.annotation.RequestParam;
import vip.ericchen.study.spring.framework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * description
 *
 * @author EricChen 2020/01/04 21:22
 */
@Controller
public class HelloController {

    @Autowired
    private IHelloService helloService;


    @RequestMapping(value = "/hello")
    public String helloWorld(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam(value = "name") String name) {
        return helloService.hello();
    }

    @RequestMapping(value = "/")
    public ModelAndView hello(@RequestParam("name") String name) {
        Map<String,Object> model = new HashMap<String,Object>();
        String result = helloService.queryUser(name);
        model.put("name", name);
        model.put("data", result);
        model.put("token", "123456");
        return new ModelAndView("hello.html",model);
    }
}
