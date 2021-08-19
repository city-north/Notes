package cn.eccto.study.springframework.tutorials.objectprovider;

/**
 * description
 *
 * @author JonathanChen 2019/11/15 20:10
 */
public class MsgBean {
    private String msg;

    public MsgBean(String msg) {
        this.msg = msg;
    }

    public void showMessage() {
        System.out.println("msg: " + msg);
    }
}