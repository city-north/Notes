package cn.eccto.study.springframework.lifecycle.destruction;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/11/27 15:53
 */
public class DemoBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DemoBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
