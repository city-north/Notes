package cn.eccto.study.java.javabeans;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/10/30 21:57
 */
public class User {
    private String username;

    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
