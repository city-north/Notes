package cn.eccto.study.springframework.tutorials.beanproxy;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author EricChen 2019/11/16 18:19
 */
class MySingletonBean {
    @Autowired
    private IPrototype prototypeBean;

    public void showMessage() {
        System.out.println("Hi, the time is " + prototypeBean.getDateTime());
    }
}
