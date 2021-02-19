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
        //mid
        int mid = (left + right) >> 1;
        doMergeSort(array, left, mid);
        doMergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    @Override
    public void merge(int[] array, int left, int mid, int right) {
        int temp[] = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= right) {
            temp[k++] = array[i] < array[j] ? array[i++] : array[j++];
        }
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= right) {
            temp[k++] = array[j++];
        }
        System.arraycopy(temp, 0, array, left, temp.length);
    }
}
