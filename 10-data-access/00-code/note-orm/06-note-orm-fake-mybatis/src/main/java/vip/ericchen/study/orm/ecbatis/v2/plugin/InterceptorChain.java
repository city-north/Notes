package vip.ericchen.study.orm.ecbatis.v2.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 拦截器链
 * </p>
 *
 * @author EricChen 2020/02/25 22:59
 */
public class InterceptorChain {
    List<Interceptor> interceptorList = new ArrayList<>();


    public void addInterceptor(Interceptor interceptor) {
        interceptorList.add(interceptor);
    }

    /**
     * 对被拦截对象进行层层代理
     *
     * @param target
     * @return
     */
    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptorList) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public boolean hasPlugin() {
        if (interceptorList.size() == 0) {
            return false;
        }
        return true;
    }
}
