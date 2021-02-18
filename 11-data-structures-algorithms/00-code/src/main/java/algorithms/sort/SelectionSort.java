package algorithms.sort;

/**
 * @author EricChen 2021/02/01 15:23
 */
public interface SelectionSort {


    default void run() {
        int[] array = {7, 4, 9, 8, 2, 9, 9, 8, 7};
        selectionSort(array, array.length);
        for (int i : array) {
            System.out.println(i);
        }
    }

     void selectionSort(int[] a, int n);


}
