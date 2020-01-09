package vip.ericchen.study.spring.framework.example;


import vip.ericchen.study.spring.framework.example.service.IHelloService;
import vip.ericchen.study.spring.framework.stereotype.Autowired;
import vip.ericchen.study.spring.framework.stereotype.Controller;

/**
 * description
 *
 * @author EricChen 2020/01/04 21:22
 */
@Controller
public class HelloController {

    @Autowired
    private IHelloService helloService;


    public String helloWorld() {
        return helloService.hello();
    }
}
