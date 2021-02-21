package algorithms.sort;

import algorithms.sort.base.RunnableSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/18 17:30
 */
public interface InsertionSort extends RunnableSort {


    @Override
    default void doSort(int[] array, int length) {
        insertionSort(array, length);
    }


    /**
     * 插入排序
     *
     * @param a 数组
     * @param n 数组大小
     */
    void insertionSort(int[] a, int n);



}
