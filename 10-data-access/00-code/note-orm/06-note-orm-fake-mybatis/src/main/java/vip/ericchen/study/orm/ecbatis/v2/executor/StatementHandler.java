package vip.ericchen.study.orm.ecbatis.v2.executor;

import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;
import vip.ericchen.study.orm.ecbatis.v2.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>
 * 专门处理 Statement
 * </p>
 *
 * @author EricChen 2020/02/25 22:35
 */
public class StatementHandler {
    private Configuration configuration;
    private ResultSetHandler resultSetHandler;

    public StatementHandler(Configuration configuration) {
        this.configuration = configuration;
        resultSetHandler = new ResultSetHandler();
    }

    public <T> T query(MappedStatement statement, Object[] parameter) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        Object result = null;

        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(statement.getSql());
            ParameterHandler parameterHandler = new ParameterHandler(preparedStatement);
            parameterHandler.setParameters(parameter);
            preparedStatement.execute();
            try {
                result = resultSetHandler.handle(preparedStatement.getResultSet(), statement.getResultType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (T) result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
        }
        // 只在try里面return会报错
        return null;
    }

    /**
     * 获取连接
     *
     * @return
     * @throws SQLException
     */
    private Connection getConnection() {
        String driver = configuration.getConfig().getString("jdbc.driver");
        String url = configuration.getConfig().getString("jdbc.url");
        String username = configuration.getConfig().getString("jdbc.username");
        String password = configuration.getConfig().getString("jdbc.password");
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
