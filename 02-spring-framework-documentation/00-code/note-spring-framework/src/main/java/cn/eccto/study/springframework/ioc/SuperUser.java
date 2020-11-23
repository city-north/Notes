package cn.eccto.study.springframework.ioc;

import cn.eccto.study.springframework.User;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/10/19 21:18
 */
public class SuperUser extends User {

    private String address;

    public String getAddress() {
        return address;
    }

    public SuperUser setAddress(String address) {
        this.address = address;
        return this;
    }

}
