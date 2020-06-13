package cn.eccto.study.java.basic.serializable;

import java.io.Serializable;

/**
 * 序列化测试 dto
 *
 * @author EricChen 2020/01/19 15:26
 */
public class SerializableDto implements Serializable {

    private static final long serialVersionUID = -123;

    private String name;
    private int age;

    private ReferenceDto referenceDto = new ReferenceDto("123", 1);

    public SerializableDto(String name, int age) {
        System.out.println("SerializableDto 的构造方法被调用了");
        this.name = name;
        this.age = age;
    }

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
        return "SerializableDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
