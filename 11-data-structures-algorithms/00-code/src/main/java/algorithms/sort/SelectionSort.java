package algorithms.sort;

import algorithms.sort.base.RunnableSort;

/**
 * @author EricChen
 */
public interface SelectionSort extends RunnableSort {


    @Override
    default void doSort(int[] array, int length) {
        selectionSort(array, length);
    }


    void selectionSort(int[] a, int n);


}
