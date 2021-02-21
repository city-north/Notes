package algorithms.sort.standard;

import algorithms.sort.HeapSort;

import java.util.PriorityQueue;

/**
 * <p>
 * 标准堆排序
 * </p>
 *
 * @author EricChen 2021/02/20 07:29
 */
public class StandardHeapSort implements HeapSort {
    PriorityQueue<Integer> q = new PriorityQueue<>();

    @Override
    public void heapSort(int[] a, int n) {
        for (int i = 0; i < n; i++) {
            q.add(a[i]);
        }
        for (int i = 0; i < n; i++) {
            a[i] = q.poll();
        }
    }

    public static void main(String[] args) {
        new StandardHeapSort().run();
    }
}
