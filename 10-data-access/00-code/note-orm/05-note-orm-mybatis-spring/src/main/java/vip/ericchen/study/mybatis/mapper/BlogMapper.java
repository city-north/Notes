package vip.ericchen.study.mybatis.mapper;

import vip.ericchen.study.mybatis.entity.Blog;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/24 21:41
 */
public interface BlogMapper {

    /**
     * 根据 Bean 查询
     *
     * @param queryBean 查询条件
     * @return
     */
    List<Blog> selectBlogByBean(Blog queryBean);

}
