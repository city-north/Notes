package algorithms.sort.standard;

import algorithms.sort.SelectionSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen
 */
public class StrandardShellSort implements SelectionSort {

    public static void main(String[] args) {
        new StrandardShellSort().run();
    }

    @Override
    public void selectionSort(int[] array, int n) {
        int gap = 1;
        while (gap < array.length / 3) {
            gap = gap * 3 + 1;
        }
        while (gap > 0) {
            for (int i = gap; i < array.length; i++) {
                int tmp = array[i];
                int j = i - gap;
                while (j >= 0 && array[j] > tmp) {
                    array[j + gap] = array[j];
                    j -= gap;
                }
                array[j + gap] = tmp;
            }
            gap = (int) Math.floor(gap / 3);
        }

    }
}
