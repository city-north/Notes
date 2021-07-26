package cn.eccto.study.springframework.tutorials.prototype;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author JonathanChen 2019/11/15 20:13
 */
public class MySingletonBean {

    @Autowired
    private MyPrototypeBean prototypeBean;

    public void showMessage() {
        System.out.println("Hi, the time is " + prototypeBean.getDateTime());
    }

    public MyPrototypeBean getPrototypeBean() {
        return prototypeBean;
    }

    public void setPrototypeBean(MyPrototypeBean prototypeBean) {
        this.prototypeBean = prototypeBean;
    }
}
