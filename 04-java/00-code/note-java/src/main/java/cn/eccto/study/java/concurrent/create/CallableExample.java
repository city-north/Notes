package cn.eccto.study.java.concurrent.create;

import java.util.concurrent.*;

/**
 * <p>
 * 使用 Callable
 * </p>
 *
 * @author Jonathan 2020/02/26 23:14
 */
public class CallableExample implements Callable<String> {
    private String name;

    public CallableExample(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        int answer = 0;
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
                answer = answer + 1;
                System.out.println(name + "->" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Integer.toString(answer);

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new CallableExample("TEST1"));
        FutureTask<String> futureTask2 = new FutureTask<>(new CallableExample("TEST2"));
        new Thread(futureTask).start();
        new Thread(futureTask2).start();
    }
}
