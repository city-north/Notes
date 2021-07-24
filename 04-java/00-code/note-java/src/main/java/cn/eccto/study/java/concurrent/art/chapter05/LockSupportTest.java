package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * LockSupport 是进入 waiting 状态,这样看来只有一种方式能进入 bolcked 状态,那就是进入 sychronized 块 或者方法
 * </p>
 *
 * @author Jonathan 2020/04/05 00:41
 */
public class LockSupportTest {

    public static void main(String[] args) {
        System.out.println("begin park");
        LockSupport.park();//   java.lang.Thread.State: WAITING (parking)
        System.out.println("end park");
    }
}
