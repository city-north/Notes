package vip.ericchen.study.orm.ecbatis.v2.plugin;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 22:59
 */
public interface Interceptor {
    /**
     * 插件的核心逻辑实现
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 对被拦截对象进行代理
     * @param target
     * @return
     */
    Object plugin(Object target);
}
