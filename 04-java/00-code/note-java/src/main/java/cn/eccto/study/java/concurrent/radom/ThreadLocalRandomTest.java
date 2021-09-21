package cn.eccto.study.java.concurrent.radom;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/07/20 13:32
 */
public class ThreadLocalRandomTest {
    public static void main(String[] args) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            System.out.println(threadLocalRandom.nextInt(5));
        }
    }
}
