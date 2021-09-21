package cn.eccto.study.springframework.tutorials.lazy;

import javax.annotation.PostConstruct;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:35
 */
public class AlwaysBeingUsedBean {

    @PostConstruct
    public void init() {
        System.out.println("AlwaysBeingUsedBean initializing");
    }
}