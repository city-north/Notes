package algorithms.sort;

import algorithms.sort.base.RunnableSort;

/**
 * <p>
 * QuickSort
 * </p>
 *
 * @author EricChen 2021/02/18 16:35
 */
public interface QuickSort extends RunnableSort {


    @Override
    default void doSort(int[] array, int length) {
        quickSort(array, 0, length -1);
    }

    void quickSort(int[] array, int begin, int end);

    /**
     * 返回一个 pivot 的位置, 且保证 pivot左边的元素都是小于pivot
     * pivot 右边的元素都是大于pivot
     */
//    default int partition(int[] array, int begin, int end) {
//        return 0;
//    }


}
