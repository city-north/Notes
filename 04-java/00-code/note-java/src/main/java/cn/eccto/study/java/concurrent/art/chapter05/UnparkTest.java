package cn.eccto.study.java.concurrent.art.chapter05;

import java.sql.SQLOutput;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/07/11 16:17
 */
public class UnparkTest {
    public static void main(String[] args) {
        System.out.println("begin park");

        //使当前线程获取到许可证
        LockSupport.unpark(Thread.currentThread());

        //再次调用 park 方法
        LockSupport.park();
        System.out.println("end park");

    }
}
