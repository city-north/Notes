package vip.ericchen.study.mybatis.entity;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/21 21:23
 */
public class AuthorAndBlog {
    Integer authorId; // 作者ID
    String authorName; // 作者名称
    List<BlogAndComment> blog; // 文章和评论列表

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<BlogAndComment> getBlog() {
        return blog;
    }

    public void setBlog(List<BlogAndComment> blog) {
        this.blog = blog;
    }
}
