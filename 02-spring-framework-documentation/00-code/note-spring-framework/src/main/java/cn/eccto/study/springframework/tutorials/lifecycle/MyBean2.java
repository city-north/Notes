package cn.eccto.study.springframework.tutorials.lifecycle;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:22
 */
public class MyBean2 {
    private OtherBean otherBean;

    public MyBean2() {
        System.out.println("MyBean2 constructor: " + this);
    }

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

    public void cleanUp() {
        System.out.println("cleanUp method");
    }
}