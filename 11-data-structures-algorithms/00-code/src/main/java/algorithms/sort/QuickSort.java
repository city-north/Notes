package algorithms.sort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/18 16:35
 */
public interface QuickSort {
    default void run() {
        int[] array = {7, 4, 9, 8, 2, 9, 9, 8, 7};
        quickSort(array, 0, array.length - 1);
        for (int i : array) {
            System.out.println(i);
        }
    }

    void quickSort(int[] array, int begin, int end);

    /**
     * 返回一个 pivot 的位置, 且保证 pivot左边的元素都是小于pivot
     * pivot 右边的元素都是大于pivot
     */
    int partition(int[] array, int begin, int end);


}
