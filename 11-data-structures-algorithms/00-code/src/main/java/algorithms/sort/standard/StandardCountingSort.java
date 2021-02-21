package algorithms.sort.standard;

import algorithms.sort.base.RunnableSort;

/**
 * <p>
 * </p>
 *
 * @author EricChen
 */
public class StandardCountingSort implements RunnableSort {

    public static void main(String[] args) {
        new StandardCountingSort().run();
    }


    @Override
    public void doSort(int[] array, int length) {
        int maxValue = array[0];
        // 找出给定数组中的最大元素(设为maxValue)
        for (int a : array) {
            maxValue = Math.max(a, maxValue);
        }
        // 2.初始化长度为max+1的数组，所有元素为0。此数组用于存储数组中元素的计数。
        int[] count = new int[maxValue + 1];
        // 3.将每个元素的计数存储在计数数组中它们各自的索引处
        for (int i = 0; i < length; i++) {
            count[array[i]]++;
        }
        //4.存储count数组中元素的累积和。它有助于将元素放入已排序数组的正确索引中。
        for (int i = 1; i <= maxValue; i++) {
            count[i] += count[i - 1];
        }
        //
        int[] output = new int[length];
        for (int i = 0; i < length; i++) {
            //5.在count数组中查找原始数组中每个元素的索引。这给出了累积计数。将元素放置在下面的图中所示的索引处。
            output[count[array[i]] - 1] = array[i];
            //6.将每个元素放到正确的位置后，将其计数减少1
            count[array[i]]--;
        }
        // Copy the sorted elements into original array
        System.arraycopy(output, 0, array, 0, length);
    }

}

