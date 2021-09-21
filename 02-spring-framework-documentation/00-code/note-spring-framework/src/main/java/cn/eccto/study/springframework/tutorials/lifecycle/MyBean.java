package cn.eccto.study.springframework.tutorials.lifecycle;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:21
 */
public class MyBean {
    private OtherBean otherBean;

    public MyBean() {
        System.out.println("MyBean constructor: " + this);
    }

    @PostConstruct
    public void myPostConstruct() {
        System.out.println("myPostConstruct()");
    }

    @Autowired
    public void setOtherBean(OtherBean otherBean) {
        System.out.println("setOtherBean(): " + otherBean);
        this.otherBean = otherBean;
    }

    public void doSomething() {
        System.out.println("doSomething()");
    }

    @PreDestroy
    public void cleanUp() {
        System.out.println("cleanUp method");
    }
}
