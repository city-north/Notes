package vip.ericchen.study.orm.ecbatis.v2.interceptor;

import vip.ericchen.study.orm.ecbatis.v2.annotation.Intercepts;
import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;
import vip.ericchen.study.orm.ecbatis.v2.plugin.Interceptor;
import vip.ericchen.study.orm.ecbatis.v2.plugin.Invocation;
import vip.ericchen.study.orm.ecbatis.v2.plugin.Plugin;

import java.util.Arrays;

/**
 * <p>
 * 我的插件
 * </p>
 *
 * @author EricChen 2020/02/25 21:13
 */
@Intercepts("query")
public class MyPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        Object[] parameter = (Object[]) invocation.getArgs()[1];
        System.out.println("插件输出：SQL：["+statement.getSql()+"]");
        System.out.println("插件输出：Parameters："+ Arrays.toString(parameter));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
