package cn.eccto.study.java.concurrent.interrupt;

/**
 * description
 *
 * @author chen 2022/01/27 9:35 PM
 */
public class InterruptedDemo1 {

    public static void main(String[] args) {
        Thread t = new MyThread();
        t.start();
        try {
            Thread.sleep(1); // 暂停1毫秒(1)
            t.interrupt(); // 中断t线程(2)
            t.join(); // 等待t线程结束(3)
        } catch (InterruptedException e) {
            System.out.println("catch the InterruptedException! ");
        }
        System.out.println("end");
    }

}

class MyThread extends Thread {
    @Override
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
        }
    }
}