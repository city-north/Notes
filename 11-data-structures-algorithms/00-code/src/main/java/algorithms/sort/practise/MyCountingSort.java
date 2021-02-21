package algorithms.sort.practise;

import algorithms.sort.CountingSort;

/**
 * <p>
 * </p>
 *
 * @author EricChen
 */
public class MyCountingSort implements CountingSort {

    public static void main(String[] args) {
        new MyCountingSort().run();
    }

    @Override
    public void doSort(int[] array, int length) {
        int maxValue = array[0];
        // Find the largest element of the array
        for (int a : array) {
            maxValue = Math.max(a, maxValue);
        }
        // Initialize count array with all zeros.
        // Store the count of each element
        int[] count = new int[maxValue + 1];
        for (int i = 0; i < length; i++) {
            count[array[i]]++;
        }
        //累加
        for (int i = 1; i <= maxValue; i++) {
            count[i] += count[i - 1];
        }
        // Store the cumulative count of each array
        // Find the index of each element of the original array in count array, and
        // place the elements in output array
        int[] output = new int[length];
        for (int i = 0; i < length; i++) {
            output[count[array[i]] - 1] = array[i];
            count[array[i]]--;
        }
        // Copy the sorted elements into original array
        System.arraycopy(output, 0, array, 0, length);
    }
}
//[0, 0, 1, 1, 2, 2, 2, 4, 6, 3]
//[0, 0, 1, 1, 2, 2, 2, 4, 6, 9]