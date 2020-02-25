package vip.ericchen.study.orm.ecbatis.v1;

import vip.ericchen.study.orm.ecbatis.demo.mapper.BlogMapper;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 20:34
 */
public class SqlSession {

    private Configuration configuration;
    private Executor executor;

    public SqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public <T> T selectOne(String Statement, Object parameter) {
        String sql = configuration.getSql(Statement);
        if (sql != null && sql != "") {
            return executor.query(sql, parameter);
        }
        return null;
    }


    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
}
