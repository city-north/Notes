package vip.ericchen.study.orm.ecbatis.demo.mapper;

import vip.ericchen.study.orm.ecbatis.demo.dto.Blog;


/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 20:37
 */
public interface BlogMapper {
    /**
     * 根据主键查询文章
     *
     * @param bid
     * @return
     */
    Blog selectBlogById(Integer bid);
}
