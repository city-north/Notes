package algorithms.sort.practise;

import algorithms.sort.MergeSort;

/**
 * <p>
 * </p>
 *
 * @author ec
 */
public class MyMergeSort implements MergeSort {

    public static void main(String[] args) {
        new MyMergeSort().run();
    }


    @Override
    public void doMergeSort(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) >> 1;
        doMergeSort(array, left, mid);
        doMergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    private void merge(int[] array, int left, int mid, int right) {
        int l = left;
        int r = mid + 1;
        int[] temp = new int[right - left + 1];
        int k = 0;
        while (l <= mid && r <= right) {
            temp[k++] = array[l] < array[r] ? array[l++] : array[r++];
        }
        while (l <= mid) {
            temp[k++] = array[l++];
        }
        while (r <= right) {
            temp[k++] = array[r++];
        }
        System.arraycopy(temp, 0, array, left, temp.length);
    }


}
