package vip.ericchen.study.orm.ecbatis.v2.session;

/**
 * <p>
 * a factory of {@link SqlSession}
 * </p>
 *
 * @author EricChen 2020/02/25 21:19
 */
public class SqlSessionFactory {
    private Configuration configuration;

    public SqlSessionFactory build(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }


    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
