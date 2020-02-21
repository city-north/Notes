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
 * 一级缓存测试
 * </p>
 *
 * @author EricChen 2020/02/21 22:09
 */
public class FirstLevelCacheTest {


    /**
     * First level Cache can be shared in the same SqlSession instance
     */
    @Test
    public void testShareInOneSqlSession() throws Exception{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper0 = session1.getMapper(BlogMapper.class);
            BlogMapper mapper1 = session1.getMapper(BlogMapper.class);
            Blog blog = mapper0.selectBlogById(1);
            System.out.println(blog);

            System.out.println("第二次查询，相同会话，获取到缓存了吗？");
            System.out.println(mapper1.selectBlogById(1));

            System.out.println("第三次查询，不同会话，获取到缓存了吗？");
            BlogMapper mapper2 = session2.getMapper(BlogMapper.class);
            System.out.println(mapper2.selectBlogById(1));

        } finally {
            session1.close();
        }
    }


    /**
     * 一级缓存失效
     * @throws IOException
     */
    @Test
    public void testCacheInvalid() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            System.out.println(mapper.selectBlogById(1));

            Blog blog = new Blog();
            blog.setBid(1);
            blog.setName("2019年1月6日14:39:58");
            mapper.updateByPrimaryKey(blog);
            session.commit();

            // 相同会话执行了更新操作，缓存是否被清空？
            System.out.println("在执行更新操作之后，是否命中缓存？");
            System.out.println(mapper.selectBlogById(1));

        } finally {
            session.close();
        }
    }

    /**
     * 因为缓存不能跨会话共享，导致脏数据出现
     * @throws IOException
     */
    @Test
    public void testDirtyRead() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper1 = session1.getMapper(BlogMapper.class);
            System.out.println(mapper1.selectBlogById(1));

            // 会话2更新了数据，会话2的一级缓存更新
            Blog blog = new Blog();
            blog.setBid(1);
            blog.setName("after modified 112233445566");
            BlogMapper mapper2 = session2.getMapper(BlogMapper.class);
            mapper2.updateByPrimaryKey(blog);
            session2.commit();

            // 其他会话更新了数据，本会话的一级缓存还在么？
            System.out.println("会话1查到最新的数据了吗？");
            System.out.println(mapper1.selectBlogById(1));
        } finally {
            session1.close();
            session2.close();
        }
    }
}
