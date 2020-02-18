package vip.ericchen.study.springjdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/13 15:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringJDBCTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testJdbcTemplate() {
        System.out.println(123);
        jdbcTemplate.execute("select * from sys_user");
    }

}
