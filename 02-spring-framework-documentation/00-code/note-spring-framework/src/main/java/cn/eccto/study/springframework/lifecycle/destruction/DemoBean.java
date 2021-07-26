package cn.eccto.study.springframework.lifecycle.destruction;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author JonathanChen 2020/11/27 15:53
 */
public class DemoBean {

    private Integer age;

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public DemoBean setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DemoBean setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "DemoBean{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
