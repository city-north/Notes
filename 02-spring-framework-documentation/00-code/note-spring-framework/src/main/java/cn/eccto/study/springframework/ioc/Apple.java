package cn.eccto.study.springframework.ioc;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/11/01 13:18
 */
public class Apple {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
