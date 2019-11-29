package cn.eccto.study.springframework.constraints;

/**
 * description
 *
 * @author EricChen 2019/11/04 23:00
 */
public class Student {
    @VaildMobile
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
