package cn.eccto.study.java.concurrent.radom;

import java.util.Random;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/07/20 11:19
 */
public class RandomTest {
    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            //随机生成一个 [0,5)之间的数
            System.out.println(random.nextInt(5));
        }
    }
}
