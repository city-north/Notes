package vip.ericchen.study.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/24 21:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {
    Integer bid; // 文章ID
    String name; // 文章标题
    Integer authorId; // 文章作者ID

}
