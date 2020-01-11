package vip.ericchen.study.spring.framework.web.servlet;


import vip.ericchen.study.spring.framework.context.ApplicationContext;
import vip.ericchen.study.spring.framework.stereotype.Controller;
import vip.ericchen.study.spring.framework.web.bind.annotation.RequestMapping;
import vip.ericchen.study.spring.framework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模仿 Spring MVC 的核心类 DispatcherServlet , 忽略掉所有的上层抽象,全部写入{@link HttpServletBean}
 *
 * @author EricChen 2020/01/11 16:08
 * @see HttpServletBean
 */
public class DispatcherServlet extends HttpServletBean {

    /**
     * List of HandlerMappings used by this servlet
     */
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<>();
    private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();


    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMapping handler = getHandler(request);
        if (handler == null) {
            response.getWriter().write("<font size='25' color='red'>404 Not Found</font><br/>");
            return;
        }
        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        ModelAndView mv = handlerAdapter.handle(request, response, handler);
        processDispatchResult(response, mv);
    }

    private void processDispatchResult(HttpServletResponse response, ModelAndView mv) throws Exception {
        //调用viewResolver的resolveView方法
        if (null == mv) {
            return;
        }

        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (ViewResolver viewResolver : this.viewResolvers) {

            if (!mv.getViewName().equals(viewResolver.getViewName())) {
                continue;
            }
            String out = viewResolver.viewResolver(mv);
            if (out != null) {
                response.getWriter().write(out);
                break;
            }
        }

    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        return this.handlerAdapters.get(handler);
    }

    private HandlerMapping getHandler(HttpServletRequest request) {
        if (handlerMappings.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (HandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }


    @Override
    protected void onRefresh(ApplicationContext applicationContext) {
        //TODO EC 监听刷新事件
        initStrategies(applicationContext);
    }


    /**
     * 初始化策略
     *
     * @param context Spring 应用上下文
     */
    protected void initStrategies(ApplicationContext context) {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    private void initFlashMapManager(ApplicationContext context) {

    }

    private void initViewResolvers(ApplicationContext context) {
        String templateRoot = context.getBeanDefinitionReader().getProperty("templateRoot");
        assert templateRoot != null;
        URL resource = this.getClass().getClassLoader().getResource(templateRoot);
        assert resource != null;
        String templateRootPath = resource.getFile();
        File templateRootDir = new File(templateRootPath);
        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new ViewResolver(template.getName(), template));
        }
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {

    }

    private void initHandlerAdapters(ApplicationContext context) {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Annotation[][] parameterAnnotations = handlerMapping.getMethod().getParameterAnnotations();

            //每一个方法有一个参数列表，那么这里保存的是形参列表
            Map<String, Integer> paramMapping = new HashMap<>();


            //这里只是出来了命名参数
            Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof RequestParam) {
                        String paramName = ((RequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramMapping.put(paramName, i);
                        }
                    }
                }
            }
            Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramMapping.put(type.getName(), i);
                }
            }
            this.handlerAdapters.put(handlerMapping, new HandlerAdapter(paramMapping));

        }
    }

    private void initHandlerMappings(ApplicationContext context) {
        List<String> beanDefinitionNames = context.getBeanDefinitionNames();
        try {
            for (String beanDefinitionName : beanDefinitionNames) {
                Object proxy = context.getBean(beanDefinitionName);
                Class<?> clazz = proxy.getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                String url = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    url = requestMapping.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + url + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(HandlerMapping.builder().controller(proxy).method(method).pattern(pattern).build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(ApplicationContext context) {

    }

    private void initLocaleResolver(ApplicationContext context) {

    }

    private void initMultipartResolver(ApplicationContext context) {

    }


    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}