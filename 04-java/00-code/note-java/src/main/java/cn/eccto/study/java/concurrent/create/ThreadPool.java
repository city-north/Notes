package cn.eccto.study.java.concurrent.create;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/06/23 21:32
 */
public class ThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> System.out.println("hello"));
        }
    }
}
