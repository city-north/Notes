package cn.eccto.study.java.basic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/07/11 15:04
 */
public class UnsafeTestUseRefection {
    /**
     * 获取 Unsafe 实例 ,
     */
    static final Unsafe unsafe ;
    /**
     * 记录变量 state 在类 TestUnsafe 中的偏移量 ,
     */
    private static final Long stateOffset;
    /**
     * 变量 state 并设置为 0
     */
     static volatile long state = 0;

    static {
        try {
            //使用反射获取 Unsafe 的成员变量 theUnsafe
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            //设置为可取
            theUnsafe.setAccessible(true);
            //获取该变量的值
            unsafe = (Unsafe) theUnsafe.get(null);
            //获取偏移量
            stateOffset = unsafe.objectFieldOffset(UnsafeTestUseRefection.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    public static void main(String[] args) {
        UnsafeTestUseRefection unsafeTest = new UnsafeTestUseRefection();
        //如果 unsafeTest 对象上的 state 变量为 0 ,则更新成 1
        final boolean b = unsafe.compareAndSwapInt(unsafeTest, stateOffset, 0, 1);
        System.out.println(b);
    }
}
