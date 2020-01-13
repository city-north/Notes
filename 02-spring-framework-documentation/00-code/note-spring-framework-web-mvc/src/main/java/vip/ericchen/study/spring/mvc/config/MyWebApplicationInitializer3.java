package vip.ericchen.study.spring.mvc.config;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * 自定义 DispatcherServlet Hierarchy 层级结构,代码方式替换 xml 配置 3
 *
 * @author EricChen 2020/01/13 19:23
 * @see MyWebApplicationInitializer2
 */
public class MyWebApplicationInitializer3 extends AbstractDispatcherServletInitializer {


    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        return context;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/app3/*"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    /**
     * 自定义注册 Servlet Filter
     */
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {new HiddenHttpMethodFilter(), new CharacterEncodingFilter() };
    }
}
