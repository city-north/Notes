package vip.ericchen.study.mybatis.service;

import vip.ericchen.study.mybatis.entity.Blog;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/24 21:36
 */
public interface IBlogService {
    /**
     * 分页查询
     */
    List<Blog> page(Integer pageNum, Integer pageSize);
}
