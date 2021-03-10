package algorithms.sort.practise;

import algorithms.sort.BubbleSort;

/**
 * <p>
 * </p>
 */
public class MyBubbleSort implements BubbleSort {
    public static void main(String[] args) {
        new MyBubbleSort().run();
    }

    @Override
    public void bubbleSort(int[] array, int n) {
        for (int i = 0; i < n; i++) {
            boolean flag = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
    }
}
