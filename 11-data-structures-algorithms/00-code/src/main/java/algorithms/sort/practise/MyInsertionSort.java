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
    public void insertionSort(int[] array, int n) {
        for (int i = 1; i < n; i++) {
            int value = array[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (array[j] >= value) {
                    array[j + 1] = array[j];
                } else {
                    break;
                }
            }
            array[j + 1] = value;
        }
    }

}
