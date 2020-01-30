package cn.eccto.study.java.collections.list;

import java.util.*;

/**
 * usage Example of RanddmAccess
 *
 * @author EricChen 2020/01/24 20:32
 * @see RandomAccess
 */
public class RandomAccessExample {


    public static void main(String[] args) {
        List<String> testArrayList = new ArrayList();
        List<String> testLinkedList = new LinkedList<>();
        for (int i = 0; i < 80000; i++) {
            testArrayList.add("" + i);
            testLinkedList.add("" + i);
        }
        System.out.println("LinkedList , 不支持 RandomAccess");
        System.out.println("for 循环遍历时间:" + traverseByNormalForLoop(testLinkedList));
        System.out.println("iterator 循环遍历时间:" + traverseByIterator(testLinkedList));

        System.out.println("ArrayList , 支持 RandomAccess");

        System.out.println("for 循环遍历时间:" + traverseByNormalForLoop(testArrayList));
        System.out.println("iterator 循环遍历时间:" + traverseByIterator(testArrayList));

        System.out.println("using random access");
        System.out.println("arrayList:" + useRandomAccess(testArrayList));
        System.out.println("LinkedList" + useRandomAccess(testLinkedList));

    }

    /**
     * 使用 for循环来迭代
     */
    private static long traverseByNormalForLoop(List<String> testList) {
        long before = System.currentTimeMillis();
        for (int i = 0; i < testList.size(); i++) {
            testList.get(i);
        }
        long after = System.currentTimeMillis();
        return after - before;
    }


    /**
     * 使用迭代器遍历
     */
    public static long traverseByIterator(List arrayList) {
        Iterator iterator = arrayList.iterator();
        long before = System.currentTimeMillis();
        while (iterator.hasNext()) {
            iterator.next();
        }
        long after = System.currentTimeMillis();
        return after - before;
    }

    /**
     * {@link RandomAccess} usage Example of RanddmAccess
     */
    public static long useRandomAccess(List arrayList) {
        if (arrayList instanceof RandomAccess) {
            //use for
            return traverseByNormalForLoop(arrayList);
        } else {
            // use an iterator
            return traverseByIterator(arrayList);
        }
    }


}
