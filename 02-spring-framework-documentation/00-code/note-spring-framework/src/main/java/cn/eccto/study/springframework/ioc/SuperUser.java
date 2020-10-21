package cn.eccto.study.springframework.ioc;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/10/19 21:18
 */
@Super
public class SuperUser extends User {

    private String address;

    public String getAddress() {
        return address;
    }

    public SuperUser setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public String toString() {
        return "SuperUser{} " + super.toString();
    }
}
