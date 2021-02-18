package algorithms.sort.standard;

import algorithms.sort.SelectionSort;

/**
 * @author EricChen 2021/02/18 17:44
 */
public class StrandSelectionSort implements SelectionSort {

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
            if (i == minIndex) {
                continue;
            }
            int temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }


}
