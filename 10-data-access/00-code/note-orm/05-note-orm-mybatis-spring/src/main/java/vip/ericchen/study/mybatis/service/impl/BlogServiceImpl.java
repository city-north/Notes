package vip.ericchen.study.mybatis.service.impl;

import org.springframework.stereotype.Service;
import vip.ericchen.study.mybatis.entity.Blog;
import vip.ericchen.study.mybatis.mapper.BlogMapper;
import vip.ericchen.study.mybatis.service.IBlogService;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/24 21:36
 */
@Service
public class BlogServiceImpl implements IBlogService {
    private BlogMapper blogMapper;

    public BlogServiceImpl(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Override
    public List<Blog> page(Integer pageNum, Integer pageSize) {
        return blogMapper.selectBlogByBean(new Blog());
    }
}
