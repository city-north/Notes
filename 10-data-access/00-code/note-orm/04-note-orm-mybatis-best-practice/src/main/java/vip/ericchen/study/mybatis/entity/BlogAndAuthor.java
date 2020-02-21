package vip.ericchen.study.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/21 20:55
 */
public class BlogAndAuthor {
    Integer bid; // 文章ID
    String name; // 文章标题
    Author author; // 作者

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BlogAndAuthor{" +
                "bid=" + bid +
                ", name='" + name + '\'' +
                ", author=" + author +
                '}';
    }
}
