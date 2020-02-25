package vip.ericchen.study.orm.ecbatis.v2.binding;

import vip.ericchen.study.orm.ecbatis.v2.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 22:23
 */
public class MapperProxy implements InvocationHandler {
    private SqlSession sqlSession;
    private Class<?> mapperInterface;

    public MapperProxy(SqlSession sqlSession, Class<?> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (method.isDefault()) {
            return method.invoke(this, args);
        }
        String mapperInterface = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String statementId = mapperInterface + "." + methodName;
        return sqlSession.selectOne(statementId, args);
    }
}
