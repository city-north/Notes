package vip.ericchen.study.mybatis;

import vip.ericchen.study.mybatis.entity.Blog;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/21 20:47
 */
public interface BlogMapperExt extends BlogMapper {
    Blog selectBlogByName(String name);
}

