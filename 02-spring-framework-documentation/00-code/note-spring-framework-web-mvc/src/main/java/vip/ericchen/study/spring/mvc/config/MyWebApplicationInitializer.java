package vip.ericchen.study.spring.mvc.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;


/**
 * 自定义 DispatcherServlet Hierarchy 层级结构,代码方式替换 xml 配置 2
 * Custom initialization DispatcherServlet , using java configuration instead of xml config file
 * Implementations of this SPI will be detected automatically by {@link org.springframework.web.SpringServletContainerInitializer},
 * Which itself is bootstrapped automatically by servlet 3.0 container
 *
 * @author EricChen 2020/01/11 17:14
 * @see DispatcherServlet
 * @see AppConfig
 */
//public class MyWebApplicationInitializer implements WebApplicationInitializer {
//
//
//    public void onStartup(ServletContext servletContext) {
//        //load spring web application
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.register(AppConfig.class);
//        context.refresh();
//
//        //create and register the dispatcherServlet
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
//        ServletRegistration.Dynamic registration = servletContext.addServlet("app", dispatcherServlet);
//        registration.setLoadOnStartup(1);
//        registration.addMapping("/*");
//    }
//}
