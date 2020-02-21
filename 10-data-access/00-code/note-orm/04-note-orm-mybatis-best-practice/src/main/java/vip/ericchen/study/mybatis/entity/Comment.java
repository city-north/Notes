package vip.ericchen.study.mybatis.entity;

public class Comment {
    Integer commentId; // 评论ID
    Integer bid; // 所属文章ID
    String content; // 内容

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", bid=" + bid +
                ", content='" + content + '\'' +
                '}';
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
