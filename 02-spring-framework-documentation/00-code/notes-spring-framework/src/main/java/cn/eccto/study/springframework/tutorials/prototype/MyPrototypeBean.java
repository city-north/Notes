package cn.eccto.study.springframework.tutorials.prototype;

import java.time.LocalDateTime;

/**
 * description
 *
 * @author EricChen 2019/11/15 20:13
 */
public class MyPrototypeBean {

    private String dateTimeString = LocalDateTime.now().toString();

    public String getDateTime() {
        return dateTimeString;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }
}