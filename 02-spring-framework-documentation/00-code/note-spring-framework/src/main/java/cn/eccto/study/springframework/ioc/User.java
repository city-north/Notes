package cn.eccto.study.springframework.ioc;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/10/19 20:46
 */
public class User {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
