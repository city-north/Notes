package algorithms.sort.standard;


import algorithms.sort.BubbleSort;

/**
 * @author ec
 */
public class StandardBubbleSort implements BubbleSort {

    @Override
    public  void bubbleSort(int[] a, int n) {
        if (n <= 1) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            // 提前退出冒泡循环的标志位
            boolean flag = false;
            for (int j = 0; j < n - i - 1; ++j) {
                // 交换
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    // 表示有数据交换
                    flag = true;
                }
            }
            // 没有数据交换，提前退出
            if (!flag) {
                break;
            }
        }
    }


}
