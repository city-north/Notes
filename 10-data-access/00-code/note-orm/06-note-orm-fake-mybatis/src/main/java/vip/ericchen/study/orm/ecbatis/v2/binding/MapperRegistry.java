package vip.ericchen.study.orm.ecbatis.v2.binding;

import vip.ericchen.study.orm.ecbatis.v1.MapperProxy;
import vip.ericchen.study.orm.ecbatis.v2.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Mapper 注册中心
 * </p>
 *
 * @author EricChen 2020/02/25 21:33
 */
public class MapperRegistry {
    //存放类和代理工厂的关系
    private Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();


    public void addMapper(Class<?> type) {
        knownMappers.put(type, new MapperProxyFactory<>(type));
    }

    //因为创建代理的时候需要 SqlSession 里的 executor去执行 sql
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<?> mapperProxyFactory = knownMappers.get(type);
        return mapperProxyFactory.newInstance(sqlSession);
    }

}
