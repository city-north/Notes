package cn.eccto.study.springframework.aop.interceptor.before;

import java.lang.reflect.Method;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author JonathanChen
 */
@FunctionalInterface
public interface BeforeInterceptor {


    /**
     * 前置通知
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数
     */
    void before(Object obj, Method method, Object... args);

}
