package cn.eccto.study.java.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <p>
 * 直接内存可以通过-XX MaxDirectMemorySize 指定,如果不指定,和 Xmx 一直
 * </p>
 *
 * @author EricChen 2020/05/02 17:57
 */
public class DirectMemoryOOM {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        //直接通过反射获取到 Unsafe 实例进行内存分配
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }
}
