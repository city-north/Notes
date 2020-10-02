package reference;

import java.lang.ref.SoftReference;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen
 */
public class SoftReferenceExample {
    public static void main(String[] args) {
        User user = new User(1, "123");
        //通过强引用建立软引用
        SoftReference<User> userSoftRef = new SoftReference<User>(user);
        //去除强引用
        user = null;
        System.out.println(userSoftRef.get());
        //进行一次垃圾收集
        System.gc();
        System.out.println("After GC");
        //获取回收之后软引用对象
        System.out.println(userSoftRef.get());
        //分配一块较大的空间,让系统资源认为内存紧张,
        byte[] b = new byte[1024 * 950 * 7];
        //进行一次GC,实际上是多余的,因为在分配大数据是,系统会自动进行GC,这里为了更清楚说明
        System.gc();
        //从软引用获取数据
        System.out.println("-"+userSoftRef.get());
    }
}
