package algorithms.sort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/18 17:16
 */
public interface BubbleSort {

    default void run() {
        int[] array = {1, 7, 5, 2, 1, 6, 2, 5, 2, 7, 3};
        bubbleSort(array, array.length);
        for (int i : array) {
            System.out.println(i);
        }
    }

    /**
     * 冒泡排序，a 表示数组，n 表示数组大小
     *
     * @param a 数组
     * @param n 数组大小
     */
    void bubbleSort(int[] a, int n);
}
