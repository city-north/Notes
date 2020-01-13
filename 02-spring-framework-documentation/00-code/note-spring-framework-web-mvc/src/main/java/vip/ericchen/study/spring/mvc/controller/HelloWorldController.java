package vip.ericchen.study.spring.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * a common controller in spring mvc
 *
 * @author EricChen 2020/01/11 17:40
 */
@Slf4j
@Controller
public class HelloWorldController {


    @RequestMapping("/hello")
    @ResponseBody
    public String helloWorld(HttpServletRequest request, String name) {
        WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(request);
        String[] beanDefinitionNames = webApplicationContext.getBeanDefinitionNames();
        log.debug("bean in servlet WebApplicationContext is [{}]", Arrays.toString(beanDefinitionNames));
        ApplicationContext parent = webApplicationContext.getParent();
        String[] beanDefinitionNamesInRoot= parent.getBeanDefinitionNames();
        log.debug("bean in root WebApplicationContext is [{}]", Arrays.toString(beanDefinitionNamesInRoot));
        return "hello ," + name;
    }
}
