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
        if (n <= 1) {
            return;
        }

        for (int i = 0; i < n - 1; i++) {
            boolean flag = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (array[i] < array[j]) {
                    swap(array, i, j);
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }

        }
    }
}
