package vip.ericchen.study.mybatis;

import org.junit.Test;
import vip.ericchen.study.mybatis.entity.Blog;

import java.io.IOException;
import java.sql.*;

/**
 * <p>
 * 传统使用 JDBC方式查询数据库
 * </p>
 *
 * @author EricChen 2020/02/21 09:46
 */
public class JdbcTest {


    /**
     * 传统 JDBC方式查询数据库的弊端很多
     *
     * @throws IOException
     */
    @Test
    public void testJdbc() throws IOException {
        Connection conn = null;
        Statement stmt = null;
        Blog blog = new Blog();

        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            long startTime = System.currentTimeMillis();    //获取开始时间
            // 打开连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/notes", "root", "root");
            // 执行查询
            stmt = conn.createStatement();
            String sql = "SELECT bid, name, author_id FROM blog";
            ResultSet rs = stmt.executeQuery(sql);
            long endTime = System.currentTimeMillis();    //获取结束时间
            System.out.println("创建链接时间：" + (endTime - startTime) + "ms");

            // 获取结果集
            while (rs.next()) {
                Integer bid = rs.getInt("bid");
                String name = rs.getString("name");
                Integer authorId = rs.getInt("author_id");
                blog.setAuthorId(authorId);
                blog.setBid(bid);
                blog.setName(name);
            }
            System.out.println(blog);
            long startTime2 = System.currentTimeMillis();    //获取开始时间
            rs.close();
            stmt.close();
            conn.close();
            long endTime2 = System.currentTimeMillis();    //获取结束时间
            System.out.println("关闭链接时间：" + (endTime2 - startTime2) + "ms");

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * 原生JDBC的批量操作方式 ps.addBatch()
     *
     * @throws IOException
     */
    @Test
    public void testJdbcBatch() throws IOException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 打开连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/notes", "root", "root");
            ps = conn.prepareStatement(
                    "INSERT into blog values (?, ?, ?)");

            for (int i = 1000; i < 1010; i++) {
                Blog blog = new Blog();
                ps.setInt(1, i);
                ps.setString(2, String.valueOf("张三" + i));
                ps.setInt(3, 1001);
                ps.addBatch();
            }
            ps.executeBatch();
//            conn.commit();
            ps.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
