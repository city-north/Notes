package algorithms.sort.practise;

import algorithms.sort.HeapSort;

import java.util.PriorityQueue;

/**
 * <p>
 * </p>
 *
 * @author EricChen
 */
public class MyHeapSort implements HeapSort {
    public static void main(String[] args) {
        new MyHeapSort().run();
    }

    PriorityQueue<Integer> queue = new PriorityQueue<>();

    @Override
    public void heapSort(int[] a, int n) {
        for (int i = 0; i < n; i++) {
            queue.add(i);
        }
        for (int i = 0; i < n; i++) {
            a[i] = queue.poll();
        }
    }
}
