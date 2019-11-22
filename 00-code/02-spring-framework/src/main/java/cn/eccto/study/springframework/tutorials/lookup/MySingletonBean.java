package cn.eccto.study.springframework.tutorials.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author EricChen2019/11/16 20:58
 */
@Component
class MySingletonBean {

    public void showMessage(){
        MyPrototypeBean bean = getPrototypeBean();
        System.out.println("Hi, the time is "+bean.getDateTime());
    }

    @Lookup
    public MyPrototypeBean getPrototypeBean(){
        //spring will override this method
        return null;
    }
}
