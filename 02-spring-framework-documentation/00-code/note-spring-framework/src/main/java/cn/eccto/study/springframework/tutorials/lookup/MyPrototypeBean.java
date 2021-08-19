package cn.eccto.study.springframework.tutorials.lookup;

import java.time.LocalDateTime;

/**
 * description
 *
 * @author JonathanChen2019/11/16 20:59
 */
class MyPrototypeBean {
    private String dateTimeString = LocalDateTime.now().toString();

    public String getDateTime() {
        return dateTimeString;
    }
}
