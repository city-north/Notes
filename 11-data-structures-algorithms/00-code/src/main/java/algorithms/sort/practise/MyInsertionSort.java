package algorithms.sort.practise;

import algorithms.sort.InsertionSort;

/**
 *
 */
public class MyInsertionSort implements InsertionSort {

    public static void main(String[] args) {
        new MyInsertionSort().run();
    }

    @Override
    public void insertionSort(int[] a, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 1; i < n; i++) {
            int value = a[i];
            int j = i - 1;
            for (; j > 0; j--) {
                if (a[j] > value) {
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            a[j + 1] = value;
        }

    }
}
