package algorithms.sort;

import algorithms.sort.base.RunnableSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/20 07:28
 */
public interface HeapSort extends RunnableSort {


    @Override
    default void doSort(int[] array, int length) {
        heapSort(array, length);
    }

    /**
     * 冒泡排序，a 表示数组，n 表示数组大小
     *
     * @param a 数组
     * @param n 数组大小
     */
    void heapSort(int[] a, int n);


}
