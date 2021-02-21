package algorithms.sort.standard;

import algorithms.sort.HeapSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/20 07:36
 */
public class StandardHeapSort2 implements HeapSort {

    public static void main(String[] args) {
        new StandardHeapSort2().run();
    }

    @Override
    public void heapSort(int[] array, int n) {
        if (n == 0) {
            return;
        }
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            swap(array, i, 0);
            heapify(array, i, 0);
        }
    }

    private void heapify(int[] array, int length, int i) {
        int left = 2 * i + 1, right = 2 * i + 2;
        int largest = i;
        if (left < length && array[left] > array[largest]) {
            largest = left;
        }
        if (right < length && array[right] > array[largest]) {
            largest = right;
        }
        if (largest != i) {
            swap(array, i, largest);
            heapify(array, length, largest);
        }
    }
}
