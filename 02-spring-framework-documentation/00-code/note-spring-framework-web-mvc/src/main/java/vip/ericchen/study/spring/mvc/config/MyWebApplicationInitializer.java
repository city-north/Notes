package vip.ericchen.study.spring.mvc.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Custom initialization DispatcherServlet , using java configuration instead of xml config file
 *
 * @author EricChen 2020/01/11 17:14
 * @see DispatcherServlet
 * @see AppConfig
 */
//public class MyWebApplicationInitializer implements WebApplicationInitializer {
//
//
//    public void onStartup(ServletContext servletContext) throws ServletException {
//
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
