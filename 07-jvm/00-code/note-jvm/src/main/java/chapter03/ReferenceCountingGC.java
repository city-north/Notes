package chapter03;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen
 */
public class ReferenceCountingGC {

    public Object instance = null;
    private static final int _1MB = 1024 * 1024;

    private byte[] bigSize = new byte[2 * _1MB];

    public static void main(String[] args) {
        testGC();
    }
    public static void testGC() {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;  //互相引用会导致引用计数算法永远不可能为 0
        objB.instance = objA;

        objA = null;
        objB = null;

        System.gc();
    }

}
