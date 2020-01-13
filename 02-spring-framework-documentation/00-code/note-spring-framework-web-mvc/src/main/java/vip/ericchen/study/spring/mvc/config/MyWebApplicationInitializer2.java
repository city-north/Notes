package vip.ericchen.study.spring.mvc.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 自定义 DispatcherServlet Hierarchy 层级结构,代码方式替换 xml 配置 2
 *
 * @author EricChen 2020/01/13 19:23
 * @see MyWebApplicationInitializer2
 */
//public class MyWebApplicationInitializer2 extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//    /**
//     * 指定一个 Root WebApplicationContext ,通常用于注册一些可以在 Servlet 之间共享的 bean
//     */
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class[]{RootConfig.class};
//    }
//
//    /**
//     * 指定一个 Servlet WebApplicationContext ,通常是这个 Servlet 独享的 bean,同时,它也继承了 root 的所有
//     */
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[]{ServletConfig.class};
//    }
//
//    /**
//     * 指定注册的 Servlet 的映射
//     */
//    @Override
//    protected String[] getServletMappings() {
//        return new String[]{"/app/*"};
//    }
//}
