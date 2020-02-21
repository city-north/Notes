package vip.ericchen.study.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import vip.ericchen.study.mybatis.entity.Blog;

import java.util.List;

/**
 * <p>
 * SpringJDbc
 * </p>
 *
 * @author EricChen 2020/02/21 20:44
 */
public class SpringJdbcTest {
    private static final String PROPERTY_PATH = "/hikari.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtilsTest.class);
    private static HikariDataSource dataSource;
    private static QueryRunner queryRunner;

    static {
        HikariConfig config = new HikariConfig(PROPERTY_PATH);
        dataSource = new HikariDataSource(config);
        queryRunner = new QueryRunner(dataSource);
    }


    @Test
    public void testSpringJdbc() {
        DbUtilsTest.init();
        String sql = "select * from blog";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Blog> query = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Blog blog = new Blog();
            blog.setAuthorId(rs.getInt("bid"));
            return blog;
        });
        System.out.println(query);
    }


}
