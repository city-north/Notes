package algorithms.sort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/18 16:20
 */
public interface MergeSort {

    default void run() {
        int[] array = {7, 4, 9, 8, 2, 9, 9, 8, 7};
        doMergeSort(array, 0, array.length - 1);
        for (int i : array) {
            System.out.println(i);
        }
    }

    void doMergeSort(int[] array, int left, int right);

    void merge(int[] array, int left, int mid, int right);
}
