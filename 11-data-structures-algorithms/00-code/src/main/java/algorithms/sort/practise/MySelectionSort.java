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
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(array, minIndex, i);
            }
        }
    }
}
