package cn.eccto.study.java.basic;

import sun.misc.Unsafe;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/07/11 15:04
 */
public class UnsafeTest {
    /**
     * 获取 Unsafe 实例 ,
     */
    static final Unsafe unsafe = Unsafe.getUnsafe();
    /**
     * 记录变量 state 在类 TestUnsafe 中的偏移量 ,
     */
    private static final Long stateOffset;
    /**
     * 变量 state 并设置为 0
     */
    volatile long state = 0;

    static {
        try {
            //获取偏移量
            stateOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    public static void main(String[] args) {
        UnsafeTest unsafeTest = new UnsafeTest();
        //如果 unsafeTest 对象上的 state 变量为 0 ,则更新成 1
        final boolean b = unsafe.compareAndSwapInt(unsafeTest, stateOffset, 0, 1);
        System.out.println(b);
    }
}
