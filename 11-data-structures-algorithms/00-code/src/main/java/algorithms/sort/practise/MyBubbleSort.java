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
    public void bubbleSort(int[] a, int n) {
        if (n <= 0) {
            return;
        }
        for (int i = 0; i < n; i++) {
            boolean flag = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }

        }
    }
}
