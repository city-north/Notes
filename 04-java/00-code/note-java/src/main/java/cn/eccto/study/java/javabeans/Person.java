package cn.eccto.study.java.javabeans;

/**
 * <p>
 * Setter and Getter </p>
 * 可写 |  可读
 * </p>
 *
 * @author EricChen 2020/10/17 20:59
 */
public class Person {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Person setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
