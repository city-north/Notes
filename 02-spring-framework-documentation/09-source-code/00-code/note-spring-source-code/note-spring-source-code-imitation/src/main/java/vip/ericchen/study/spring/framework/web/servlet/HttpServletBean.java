package vip.ericchen.study.spring.framework.web.servlet;

import vip.ericchen.study.spring.framework.context.ApplicationContext;
import vip.ericchen.study.spring.framework.context.support.GenericApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 模仿 Spring MVC 中的 {@code HttpServletBean} 和 {@code FrameworkServlet} 类相关的属性
 * <p>
 * {@code HttpServletBean} 主要作用是,处理 {@code web.xml} 文件中的 {@code servlet} 标签内的 {@code init-params} 属性
 * {@code FrameworkServlet} 主要作用是 为 Spring web框架 做了基本的集成
 * <p>
 * Simple extension of {@link javax.servlet.http.HttpServlet} which treats
 * its config parameters  ({@code init-param} entries within the
 * {@code servlet} tag in {@code web.xml}) as bean properties.
 *
 * @author EricChen 2020/01/11 16:10
 */
public abstract class HttpServletBean extends HttpServlet {
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

    private ApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        String initParameter = config.getInitParameter(CONFIG_LOCATION_PARAM);
        assert initParameter != null;
        applicationContext = new GenericApplicationContext(initParameter);
        onRefresh(applicationContext);
    }

    protected abstract void onRefresh(ApplicationContext applicationContext);



    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected final void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }


    protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
