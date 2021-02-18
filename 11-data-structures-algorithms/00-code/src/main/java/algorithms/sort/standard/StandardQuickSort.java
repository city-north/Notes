package algorithms.sort.standard;

import algorithms.sort.QuickSort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/18 12:29
 */
public class StandardQuickSort implements QuickSort {

    @Override
    public  void quickSort(int[] array, int begin, int end) {
        if (end <= begin) {
            return;
        }
        int pivot = partition(array, begin, end);
        quickSort(array, begin, pivot - 1);
        quickSort(array, pivot + 1, end);
    }

    /**
     * 返回一个 pivot 的位置, 且保证 pivot左边的元素都是小于pivot , pivot 右边的元素都是大于pivot
     */
    @Override
    public  int partition(int[] array, int begin, int end) {
        //pivot标杆位置 , counter 小于 pivot 的元素的个数
        //pivot选最后一个
        int pivot = end;
        //小于pivot的元素的个数,也表示最后pivot的位置
        int counter = begin;
        for (int i = begin; i < end; i++) {
            if (array[i] < array[pivot]) {
                //小于标杆 ,交换 counter
                int temp = array[counter];
                array[counter] = array[i];
                array[i] = temp;
                counter++;
            }
        }
        //调换 pivot 和 counter , counter左边是小于 pivot 的值
        int temp = array[pivot];
        array[pivot] = array[counter];
        array[counter] = temp;
        return counter;
    }




}
