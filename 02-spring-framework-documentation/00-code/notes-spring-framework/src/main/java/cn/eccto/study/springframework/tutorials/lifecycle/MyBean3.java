package cn.eccto.study.springframework.tutorials.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author EricChen 2019/11/14 20:26
 */
public class MyBean3 implements InitializingBean, DisposableBean {
    private OtherBean otherBean;

    public MyBean3() {
        System.out.println("MyBean3 constructor: " + this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet()");
    }

    @Autowired
    public void setOtherBean(OtherBean otherBean) {
        System.out.println("setOtherBean(): " + otherBean);
        this.otherBean = otherBean;
    }

    public void doSomething() {
        System.out.println("doSomething() :");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy() method");
    }
}
