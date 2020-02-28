package cn.eccto.study.java.concurrent.art.chapter02;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/28 13:52
 */
public class VolatileDemo {

//    public static boolean stop = false;
    public volatile static boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (!stop) {
                i++;
            }
            System.out.println(i);
        });
        thread.start();
        Thread.sleep(1000);
        stop = true;
    }
}
