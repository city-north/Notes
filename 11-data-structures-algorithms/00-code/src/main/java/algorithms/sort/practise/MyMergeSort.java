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

    private void merge(int[] array, int begin, int mid, int end) {
        int[] temp = new int[end - begin + 1];
        int L = begin;
        int R = mid + 1;
        int k = 0;
        while (L <= mid && R <= end) {
            temp[k++] = array[L] < array[R] ? array[L++] : array[R++];
        }
        while (L <= mid) {
            temp[k++] = array[L++];
        }
        while (R <= end) {
            temp[k++] = array[R++];
        }
        System.arraycopy(temp, 0, array, begin, temp.length);
    }


}
