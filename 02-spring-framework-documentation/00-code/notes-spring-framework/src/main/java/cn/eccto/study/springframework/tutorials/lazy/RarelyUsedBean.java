package cn.eccto.study.springframework.tutorials.lazy;

import javax.annotation.PostConstruct;

/**
 * description
 *
 * @author EricChen 2019/11/14 20:35
 */
public class RarelyUsedBean {

    @PostConstruct
    private void initialize() {
        System.out.println("RarelyUsedBean initializing");
    }

    public void doSomething() {
        System.out.println("RarelyUsedBean method being called");
    }
}