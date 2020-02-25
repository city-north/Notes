package vip.ericchen.study.orm.ecbatis.v2.executor;

import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;
import vip.ericchen.study.orm.ecbatis.v2.session.Configuration;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 21:43
 */
public class SimpleExecutor implements Executor {
    private Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T query(MappedStatement statement, Object[] parameter) {
        return new StatementHandler(configuration).query(statement,parameter);
    }
}
