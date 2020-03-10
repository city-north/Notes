package cn.eccto.study.java.concurrent.condition;

import java.util.ArrayList;

/**
 * <p>
 * Example of {@link TaskQueue}
 * </p>
 *
 * @author EricChen 2020/03/10 15:54
 */
public class TaskQueueExample {
    public static void main(String[] args) {
        TaskQueue q = new TaskQueue();
        ArrayList ts = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    String task = q.getTask();
                    System.out.println(String.format("executing task %s", task));
                }
            });
            t.start();
            ts.add(t);
        }
    }
}
