package vip.ericchen.study.orm.ecbatis.v1;

import java.lang.reflect.Proxy;
import java.util.ResourceBundle;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 20:44
 */
public class Configuration {

    public final ResourceBundle sqlMappings;

    public Configuration(String properties) {
        sqlMappings = ResourceBundle.getBundle(properties);

    }

    /**
     * 通过配置获取代理对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type,SqlSession sqlSession) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{type},
                new MapperProxy(sqlSession));
    }

    public String getSql(String statement) {
        return sqlMappings.getString(statement);
    }
}
