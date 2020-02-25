package vip.ericchen.study.orm.ecbatis.v2.binding;

import vip.ericchen.study.orm.ecbatis.v2.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 21:34
 */
public class MapperProxyFactory<T> {
    private Class<T> mapperInterface;
    private Class object;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public <T> T newInstance(SqlSession sqlSession) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{mapperInterface},
                new MapperProxy(sqlSession, mapperInterface));
    }
}
