package cn.eccto.study.springframework.tutorials.beanproxy;

import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Provider;
/**
 * description
 *
 * @author EricChen 2019/11/16 20:19
 */
class MyNarrowerSingletonBean {
    @Autowired
    private Provider<MyPrototypeBean> provider;

    public void showMessage() {
        System.out.println("Hi, the time is " + provider.get().getDateTime());
    }
}
