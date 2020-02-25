package vip.ericchen.study.orm.ecbatis.demo;

import vip.ericchen.study.orm.ecbatis.demo.dto.Blog;
import vip.ericchen.study.orm.ecbatis.demo.mapper.BlogMapper;
import vip.ericchen.study.orm.ecbatis.v1.Configuration;
import vip.ericchen.study.orm.ecbatis.v1.Executor;
import vip.ericchen.study.orm.ecbatis.v1.SqlSession;

/**
 * <p>
 * EcBatis 测试
 * </p>
 *
 * @author EricChen 2020/02/25 20:01
 */
public class EcBatisTest {

    public static void main(String[] args) {
        Configuration configuration = new Configuration("ecbatis-v1");
        SqlSession sqlSession = new SqlSession(configuration,new Executor());
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlogById(1);
        System.out.println(blog);
    }
}
