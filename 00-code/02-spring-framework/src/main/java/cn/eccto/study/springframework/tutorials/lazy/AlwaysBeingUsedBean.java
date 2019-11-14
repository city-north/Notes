package cn.eccto.study.springframework.tutorials.lazy;

import javax.annotation.PostConstruct;

/**
 * description
 *
 * @author qiang.chen04@hand-china.com 2019/11/14 12:35
 */
public class AlwaysBeingUsedBean {

    @PostConstruct
    public void init() {
        System.out.println("AlwaysBeingUsedBean initializing");
    }
}