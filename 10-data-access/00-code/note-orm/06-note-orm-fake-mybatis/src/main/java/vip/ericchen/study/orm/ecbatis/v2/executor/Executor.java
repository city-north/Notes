package vip.ericchen.study.orm.ecbatis.v2.executor;

import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;

/**
 * <p>
 * Executor
 * </p>
 *
 * @author EricChen 2020/02/25 21:26
 */
public interface Executor {

    /**
     * 执行查询
     */
    <T> T query(MappedStatement statement, Object[] parameter);
}
