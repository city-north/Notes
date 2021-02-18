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
    public void selectionSort(int[] a, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 0; i < n; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = a[minIndex];
                a[minIndex] = a[i];
                a[i] = temp;
            }
        }
    }
}
