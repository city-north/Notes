package chapter02.proxy.cglibProxy;

import chapter02.proxy.common.UserService;
import chapter02.proxy.common.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class UserServiceCglibProxyFactory implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(UserServiceCglibProxyFactory.class);


    /**
     * 获取Cglib代理对象
     * @return
     */
    public UserService createProxy(Class superclass){
        //创建Cglib核心类
        Enhancer enhancer = new Enhancer();
        //Cglib代理是基于继承的代理
        enhancer.setSuperclass(superclass);
        //设置回调，需要传入一个Callback的接口
        enhancer.setCallback(this);
        //生成代理
        UserService userService = (UserService) enhancer.create();
        return userService;
    }

    /**
     *
     * 只记录save方法
     * @param proxyobj 代理对象
     * @param method 方法名
     * @param args 代理方法的参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    public Object intercept(Object proxyobj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object o = methodProxy.invokeSuper(proxyobj, args);
        if ("save".equals(method.getName())){
            logger.info("chapter02.proxy.cglibProxy.UserServiceCglibProxyFactory.createProxy");
            return o;
        }

        return o;
    }
}
