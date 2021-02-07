package algorithms.sort;

/**
 * @author EricChen 2021/02/01 15:23
 */
public class SelectionSort {


    public static void main(String[] args) {
        int[] array = {7, 4, 9, 8, 2, 9, 9, 8, 7};
        selectionSort2(array, array.length);
        for (int i : array) {
            System.out.println(i);
        }
    }

    public static void selectionSort(int[] a, int n) {
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


    public static void selectionSort2(int[] a, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 0; i < n; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[i]) {
                    minIndex = j;
                }
            }
            if (minIndex == i) {
                continue;
            }
            int temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }
}
