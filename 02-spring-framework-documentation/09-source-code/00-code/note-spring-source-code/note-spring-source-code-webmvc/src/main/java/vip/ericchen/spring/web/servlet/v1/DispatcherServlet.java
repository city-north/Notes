package vip.ericchen.spring.web.servlet.v1;

import vip.ericchen.spring.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author EricChen 2020/01/04 19:53
 */
public class DispatcherServlet extends HttpServlet {
    private Properties contextConfig = new Properties();

    private List<String> className = new ArrayList<>();

    private Map<String, Object> container = new HashMap<>();

    private Map<String, MappingHandler> handlerMapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Map<String, String[]> parameterMap = req.getParameterMap();
        MappingHandler mappingHandler = handlerMapping.get(pathInfo);

        Object instance = mappingHandler.getInstance();
        Method method = mappingHandler.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] paramValue = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            if (type == HttpServletRequest.class) {
                paramValue[i] = req;
            } else if (type == HttpServletResponse.class) {
                paramValue[i] = resp;
            } else {
                RequestParam anno = parameter.getAnnotation(RequestParam.class);
                String value = anno.value();
                String[] strings = parameterMap.get(value);
                for (String val : strings) {
                    if (val != null) {
                        paramValue[i] = val;
                        break;
                    }
                }
            }

        }

        try {
            Object invoke = method.invoke(instance, paramValue);
            resp.getWriter().write(invoke.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //扫描类
        doScanner(contextConfig.getProperty("scanPackage"));
        //初始化扫描到的类,放入到 IoC容器中
        doRegister();
        //完成依赖注入
        doAutoWired();
        //初始化 HandlerMapping
        initHandlerMapping();

        System.out.println("EC framework inited");

    }

    private void initHandlerMapping() {
        try {
            for (String classname : className) {
                Class<?> instance = Class.forName(classname);
                Method[] methods = instance.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String path = annotation.value();
                        if (!"".equals(path)) {
                            Object ins = container.get(classname);
                            handlerMapping.put(path, new MappingHandler(path, ins, method));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doAutoWired() {
        container.forEach((beanName, instance) -> {
            Field[] declaredFields = instance.getClass().getDeclaredFields();
            Arrays.stream(declaredFields).filter(field -> {
                Annotation annotation = field.getAnnotation(Autowired.class);
                return annotation != null;
            }).forEach(field -> {
                String beanKey = field.getType().getName();
                Object bean = container.get(beanKey);
                field.setAccessible(true);
                if (bean != null) {
                    try {
                        field.set(instance, bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        });


    }

    private void doRegister() {
        try {
            for (String classname : className) {
                Class<?> aClass = Class.forName(classname);
                if (aClass.isAnnotationPresent(Controller.class)) {
                    Object instance = aClass.newInstance();
                    container.put(lowerFirstCase(aClass.getSimpleName()), instance);
                    container.put(classname, instance);
                } else if (aClass.isAnnotationPresent(Service.class)) {
                    Service annotation = aClass.getAnnotation(Service.class);
                    String beanName = annotation.value();
                    if ("".equals(beanName)) {
                        beanName = lowerFirstCase(aClass.getSimpleName());
                    }
                    Object instance = aClass.newInstance();
                    container.put(beanName, instance);
                    Class<?>[] interfaces = instance.getClass().getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        container.put(anInterface.getName(), instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String lowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation.replace("classpath:", ""));
        try {
            contextConfig.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描 Bean
     *
     * @param scanPackage 指定路径
     */
    private void doScanner(String scanPackage) {
        //vip.ericchen.spring.web
        String pkg = scanPackage.replaceAll("\\.", File.separator);
        URL resource = this.getClass().getClassLoader().getResource(pkg);
        File fileDir = new File(resource.getFile());
        //迭代文件
        File[] files = fileDir.listFiles();
        for (File f : fileDir.listFiles()) {
            if (f.isDirectory()) {
                doScanner(scanPackage + "." + f.getName());
            } else {
                className.add(scanPackage + "." + f.getName().replace(".class", ""));
            }
        }
    }

    class MappingHandler {
        private String path;
        private Object instance;
        private Method method;

        public MappingHandler(String path, Object instance, Method method) {
            this.path = path;
            this.instance = instance;
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Object getInstance() {
            return instance;
        }

        public void setInstance(Object instance) {
            this.instance = instance;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }
}
