package cn.eccto.study.springframework.aop.interceptor.after;

import java.lang.reflect.Method;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen
 */
@FunctionalInterface
public interface AfterInterceptor {


    /**
     * 后置通知
     *
     * @param obj          对象
     * @param method       方法
     * @param args         参数
     * @param returnResult 返回值
     */
    void after(Object obj, Method method, Object[] args, Object returnResult);
}
