package cn.eccto.study.java.concurrent.art.chapter07;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * <p>
 * A Test example of {@link java.util.concurrent.atomic.AtomicInteger}
 * </p>
 *
 * @author EricChen 2020/04/07 12:52
 */
public class AtomicIntegerArrayTest implements Runnable {
    static int[] value = new int[]{1, 2};

    static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(value);
    static int[] notAtomicIntegerArray = new int[]{2, 3};


    public static void main(String[] args) {

    }


    @Override
    public void run() {

    }
}
