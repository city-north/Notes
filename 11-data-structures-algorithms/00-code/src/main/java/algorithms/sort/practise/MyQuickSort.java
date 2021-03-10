package algorithms.sort.practise;

import algorithms.sort.QuickSort;

/**
 *
 */
public class MyQuickSort implements QuickSort {

    public static void main(String[] args) {
        new MyQuickSort().run();
    }

    @Override
    public void quickSort(int[] array, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int pivot = partition(array, begin, end);
        quickSort(array, begin, pivot - 1);
        quickSort(array, pivot + 1, end);
    }

    private int partition(int[] array, int begin, int end) {
        int pivot = end;
        int counter = begin;
        for (int i = counter; i < end; i++) {
            if (array[i] < array[pivot]) {
                swap(array, i, counter);
                counter++;
            }
        }
        swap(array, pivot, counter);
        return counter;
    }


}
