package algorithms.sort.standard;

import algorithms.sort.MergeSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/04 14:53
 */
public class StandardMergeSort implements MergeSort {

    public static void main(String[] args) {
        int[] array = {7, 4, 9, 8, 2, 9, 9, 8, 7};
        new StandardMergeSort().doMergeSort(array, 0, array.length - 1);
        for (int i : array) {
            System.out.println(i);
        }
    }

    @Override
    public void doMergeSort(int[] array, int left, int right) {
        if (right <= left) {
            return;
        }
        //  (left + right )/2
        int mid = (left + right) >> 1;
        doMergeSort(array, left, mid);
        doMergeSort(array, mid + 1, right);
        merge(array, left, mid, right);

    }

    /**
     * 一个数组, left - mid 已经有序了, mid  - right 也已经有序了, 怎么合并到一起
     */
    public void merge(int[] array, int left, int mid, int right) {
        //中间数组 temp
        int[] temp = new int[right - left + 1];
        int i = left;   //第一个数组的起始位置
        int j = mid + 1; //第二个数组的起始位置
        int k = 0; // temp 数组已经填入的数据
        //两个有序数组合成一个数组, 一定是三个 while

        //-- 把两个数组从起始位置开始, 依次网tem里挪
        while (i <= mid && j <= right) {
            temp[k++] = array[i] <= array[j] ? array[i++] : array[j++];
        }
        //--
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        //--
        while (j <= right) {
            temp[k++] = array[j++];
        }
//        for (int p = 0; p < temp.length; p++) {
//            array[left + p] = temp[p];
//        }
        System.arraycopy(temp, 0, array, left, temp.length);
    }


}
