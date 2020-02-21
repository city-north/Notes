package vip.ericchen.study.mybatis;

import org.apache.ibatis.session.RowBounds;
import vip.ericchen.study.mybatis.entity.AuthorAndBlog;
import vip.ericchen.study.mybatis.entity.Blog;
import vip.ericchen.study.mybatis.entity.BlogAndAuthor;
import vip.ericchen.study.mybatis.entity.BlogAndComment;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/21 12:59
 */
public interface BlogMapper {

    /**
     * 新增博客
     *
     * @param blog
     * @return
     */
    int insertBlog(Blog blog);

    /**
     * 根据 Bean 查询
     *
     * @param queryBean 查询条件
     * @return
     */
    List<Blog> selectBlogByBean(Blog queryBean);

    /**
     * 使用逻辑分页
     *
     * @param rb
     * @return
     */
    List<Blog> selectBlogList(RowBounds rb);


    /**
     * 根据主键查询文章
     *
     * @param bid
     * @return
     */
    Blog selectBlogById(Integer bid);

    /**
     * 一对一查询
     */
    BlogAndAuthor selectBlogWithAuthorResult(int i);


    /**
     * 根据博客查询作者，一对一，嵌套查询，存在N+1问题
     *
     */
    BlogAndAuthor selectBlogWithAuthorQuery(Integer bid);


    /**
     * 查询文章带出文章所有评论（一对多）
     */
     BlogAndComment selectBlogWithCommentById(Integer bid);


    /**
     * 查询作者带出博客和评论（多对多）
     * @return
     */
    List<AuthorAndBlog> selectAuthorWithBlog();

    /**
     * 更新博客
     * @param blog
     */
    void updateByPrimaryKey(Blog blog);
}

