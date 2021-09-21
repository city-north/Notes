package cn.eccto.study.java.jvm;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/04/28 20:00
 */
public class TestMemory {
    private String name ="12";

    public String getName() {
        return name;
    }

    public TestMemory setName(String name) {
        this.name = name;
        return this;
    }
}
