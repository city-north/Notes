package algorithms.sort.practise;

import algorithms.sort.SelectionSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/18 17:45
 */
public class MySelectionSort implements SelectionSort {

    public static void main(String[] args) {
        new MySelectionSort().run();
    }

    @Override
    public void selectionSort(int[] array, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[i]) {
                    min = j;
                }
            }
            if (min != i) {
                swap(array, i, min);
            }
        }
    }
}
