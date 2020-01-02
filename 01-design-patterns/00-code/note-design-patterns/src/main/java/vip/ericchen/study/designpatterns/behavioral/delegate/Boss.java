package vip.ericchen.study.designpatterns.behavioral.delegate;

/**
 * BOSS ,只发布命令,让 leader 来给他干活,不知道谁干活
 *
 * @author EricChen 2020/01/02 21:23
 */
public class Boss {


    public void build(String job, String command) {
        new Leader().dispatcher(job, command);
    }

}
