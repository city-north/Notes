package algorithms.sort;

/**
 * <p>
 * </p>
 */
public class InsertionSort {

    /**
     * 插入排序
     *
     * @param a 数组
     * @param n 数组大小
     */
    public static void insertionSort(int[] a, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 1; i < n; ++i) {
            int value = a[i];
            int j = i - 1;
            // 查找插入的位置
            for (; j >= 0; --j) {
                if (a[j] > value) {
                    // 数据移动
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            // 插入数据
            a[j + 1] = value;
        }
    }

    public static void insertionSort2(int[] a, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 1; i < n; i++) {
            int value = a[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (a[j] > value) {
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            a[j + 1] = value;
        }
    }

    public static void main(String[] args) {
        int[] array = {7, 4, 9, 8, 2, 9, 9, 8, 7};
        insertionSort2(array, array.length);
        for (int i : array) {
            System.out.println(i);
        }
    }

}
