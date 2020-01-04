package vip.ericchen.spring.demo;

import vip.ericchen.spring.annotation.Autowired;
import vip.ericchen.spring.annotation.Controller;
import vip.ericchen.spring.annotation.RequestMapping;
import vip.ericchen.spring.annotation.RequestParam;
import vip.ericchen.spring.demo.service.IHelloService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
