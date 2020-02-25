package vip.ericchen.study.orm.ecbatis.v2.executor;

import vip.ericchen.study.orm.ecbatis.v2.cache.CacheKey;
import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 21:52
 */
public class CachingExecutor implements Executor {

    private Executor delegate;
    private static final Map<Integer, Object> cache = new HashMap<>();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T query(MappedStatement statement, Object[] parameter) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement);
        cacheKey.update(joinStr(parameter));
        // 是否拿到缓存
        if (cache.containsKey(cacheKey.getCode())) {
            // 命中缓存
            System.out.println("【命中缓存】");
            return (T) cache.get(cacheKey.getCode());
        } else {
            // 没有的话调用被装饰的SimpleExecutor从数据库查询
            Object obj = delegate.query(statement, parameter);
            cache.put(cacheKey.getCode(), obj);
            return (T) obj;
        }
    }

    // 为了命中缓存，把Object[]转换成逗号拼接的字符串，因为对象的HashCode都不一样
    public String joinStr(Object[] objs) {
        StringBuffer sb = new StringBuffer();
        if (objs != null && objs.length > 0) {
            for (Object objStr : objs) {
                sb.append(objStr.toString() + ",");
            }
        }
        int len = sb.length();
        if (len > 0) {
            sb.deleteCharAt(len - 1);
        }
        return sb.toString();
    }
}
