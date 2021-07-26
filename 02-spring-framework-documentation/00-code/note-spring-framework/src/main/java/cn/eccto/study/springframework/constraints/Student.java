package cn.eccto.study.springframework.constraints;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

/**
 * description
 *
 * @author JonathanChen 2019/11/04 23:00
 */
@Data
public class Student implements InitializingBean {
    @VaildMobile
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("[===a");
    }
}
