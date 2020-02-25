package vip.ericchen.study.orm.ecbatis.v2.session;

import vip.ericchen.study.orm.ecbatis.demo.mapper.BlogMapper;

/**
 * <p>
 * 代表一次 Sql 会话
 * </p>
 *
 * @author EricChen 2020/02/25 21:16
 */
public interface SqlSession {


    /**
     * 获取 Mapper实例
     */
    <T> T getMapper(Class<T> type);


    /**
     * 查询
     */
    <T> T selectOne(String statement, Object parameter);


    Configuration getConfiguration();
}
