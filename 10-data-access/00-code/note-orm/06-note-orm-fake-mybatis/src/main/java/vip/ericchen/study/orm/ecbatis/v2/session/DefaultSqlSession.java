package vip.ericchen.study.orm.ecbatis.v2.session;

import vip.ericchen.study.orm.ecbatis.v2.executor.Executor;
import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 21:17
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Executor executor;


    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        // 根据全局配置决定是否使用缓存装饰
        this.executor = configuration.newExecutor();

    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        MappedStatement statement = getConfiguration().getMappedStatement(statementId);
        return executor.query(statement, (Object[]) parameter);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}
