package vip.ericchen.study.orm.ecbatis.v2;

import vip.ericchen.study.orm.ecbatis.demo.dto.Blog;
import vip.ericchen.study.orm.ecbatis.demo.mapper.BlogMapper;
import vip.ericchen.study.orm.ecbatis.v2.session.Configuration;
import vip.ericchen.study.orm.ecbatis.v2.session.SqlSession;
import vip.ericchen.study.orm.ecbatis.v2.session.SqlSessionFactory;

/**
 * <p>
 * ECBatis 测试类
 * </p>
 *
 * @author EricChen 2020/02/25 21:11
 */
public class EcbatisTest {
    public static void main(String[] args) {
        Configuration configuration = new Configuration("ecbatis-v2");
        SqlSession sqlSession = new SqlSessionFactory().build(configuration).openSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlogById(1);
        System.out.println(blog);
        Blog blog2 = blogMapper.selectBlogById(1);
        System.out.println(blog);
    }
}
