package vip.ericchen.study.mybatis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.ericchen.study.mybatis.entity.Blog;
import vip.ericchen.study.mybatis.service.IBlogService;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/24 21:32
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    private IBlogService blogService;

    private static final String DEFAULT_PAGE_NUM = "0";
    private static final String DEFAULT_PAGE_SIZE = "10";


    @Autowired
    public BlogController(IBlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<List<Blog>> blogList(@RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUM) Integer pageNum,
                                               @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        return ResponseEntity.ok(blogService.page(pageNum,pageSize));
    }

}

