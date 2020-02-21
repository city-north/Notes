package vip.ericchen.study.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.mybatis.entity.Blog;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * <p>
 * DbUtils 测试
 * </p>
 *
 * @author EricChen 2020/02/21 10:14
 */
public class DbUtilsTest {
    private static final String PROPERTY_PATH = "/hikari.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtilsTest.class);
    private static HikariDataSource dataSource;
    private static QueryRunner queryRunner;

    public static void init() {
        HikariConfig config = new HikariConfig(PROPERTY_PATH);
        dataSource = new HikariDataSource(config);
        queryRunner = new QueryRunner(dataSource);
    }

    @Test
    public void test() throws Exception {
        DbUtilsTest.init();
        QueryRunner queryRunner = getQueryRunner();
        String sql = "select * from blog where bid = ? ";
        Object[] params = new Object[]{1L};
        Blog blog = queryRunner.query(sql, new BeanHandler<>(Blog.class), params);
        System.out.println(blog);

    }



    public static QueryRunner getQueryRunner() {
        check();
        return queryRunner;
    }

    public static Connection getConnection() {
        check();
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void check() {
        if (dataSource == null || queryRunner == null) {
            throw new RuntimeException("DataSource has not been init");
        }
    }
}
