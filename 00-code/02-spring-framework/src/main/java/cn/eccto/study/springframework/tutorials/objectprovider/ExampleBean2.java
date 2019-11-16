package cn.eccto.study.springframework.tutorials.objectprovider;

/**
 * description
 *
 * @author EricChen 2019/11/15 14:20
 */
public class ExampleBean2 {
    private String arg;

    public ExampleBean2(String arg) {
        this.arg = arg;
    }

    public void doSomething() {
        System.out.println("in example bean2, arg: " + arg);
    }
}