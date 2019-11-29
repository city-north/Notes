package cn.eccto.study.springframework.tutorials.beanproxy;

import java.time.LocalDateTime;

/**
 * description
 *
 * @author EricChen 2019/11/16 20:17
 */
class MyPrototypeBean implements IPrototype {
    private String dateTimeString = LocalDateTime.now().toString();


    @Override
    public String getDateTime() {
        return dateTimeString;
    }
}
