package cn.eccto.study.sb.registservlet;

import cn.eccto.study.sb.registservlet.filter.MyFilter;
import cn.eccto.study.sb.registservlet.servlet.MyServlet;
import cn.eccto.study.sb.registservlet.servlet.MyServletListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContextListener;
import java.util.Arrays;

/**
 * Spring 提供了三个类让我们手动注册 Servlet 组件
 *
 * @author EricChen 2019/12/09 21:36
 */
@SpringBootApplication
public class RegistServletExample {

    public static void main(String[] args) {
        SpringApplication.run(RegistServletExample.class, args);
    }


    @Bean
    public ServletRegistrationBean servletServletRegistrationBean() {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new MyServlet());
        srb.setUrlMappings(Arrays.asList("/path2/*"));
        return srb;
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new MyFilter());
        frb.setUrlPatterns(Arrays.asList("/*"));
        return frb;
    }

    @Bean
    ServletListenerRegistrationBean<ServletContextListener> myServletListener() {
        ServletListenerRegistrationBean<ServletContextListener> srb =
                new ServletListenerRegistrationBean<>();
        srb.setListener(new MyServletListener());
        return srb;
    }
}
