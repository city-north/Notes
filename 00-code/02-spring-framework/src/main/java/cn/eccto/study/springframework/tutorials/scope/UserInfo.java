package cn.eccto.study.springframework.tutorials.scope;

/**
 * description
 *
 * @author EricChen 2019/11/14 15:03
 */
public class UserInfo {

    private String email;
    private String password;
    //more info

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}