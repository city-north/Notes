package vip.ericchen.study.mybatis.cache;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import vip.ericchen.study.mybatis.BlogMapper;
import vip.ericchen.study.mybatis.entity.Blog;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/21 22:51
 */
public class SecondLevelCacheTest {
    /**
     * 测试二级缓存一定要打开二级缓存开关
     *
     * @throws IOException
     */
    @Test
    public void testCache() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper1 = session1.getMapper(BlogMapper.class);
            System.out.println(mapper1.selectBlogById(1));
            // 事务不提交的情况下，二级缓存会写入吗？
            session1.commit();

            System.out.println("第二次查询");
            BlogMapper mapper2 = session2.getMapper(BlogMapper.class);
            System.out.println(mapper2.selectBlogById(1));
        } finally {
            session1.close();
        }
    }

    /**
     * 测试二级缓存在更新操作下是否会同步使缓存失效
     * 测试二级缓存一定要打开二级缓存开关
     * @throws IOException
     */
    @Test
    public void testCacheInvalid() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        SqlSession session3 = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper1 = session1.getMapper(BlogMapper.class);
            BlogMapper mapper2 = session2.getMapper(BlogMapper.class);
            BlogMapper mapper3 = session3.getMapper(BlogMapper.class);
            System.out.println(mapper1.selectBlogById(1));
            session1.commit();

            // 是否命中二级缓存
            System.out.println("是否命中二级缓存？");
            System.out.println(mapper2.selectBlogById(1));

            Blog blog = new Blog();
            blog.setBid(1);
            blog.setName("2019年1月6日15:03:38");
            mapper3.updateByPrimaryKey(blog);
            session3.commit();

            System.out.println("更新后再次查询，是否命中二级缓存？");
            // 在其他会话中执行了更新操作，二级缓存是否被清空？
            System.out.println(mapper2.selectBlogById(1));

        } finally {
            session1.close();
            session2.close();
            session3.close();
        }
    }
}
