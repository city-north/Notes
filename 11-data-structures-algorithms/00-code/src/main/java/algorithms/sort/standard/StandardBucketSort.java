package algorithms.sort.standard;

import algorithms.sort.BucketSort;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <p>
 * </p>
 *
 * @author EricChen
 */
public class StandardBucketSort implements BucketSort {

    public static void main(String[] args) {
//        StandardBucketSort b = new StandardBucketSort();
//        float[] arr = {(float) 0.42, (float) 0.32, (float) 0.33, (float) 0.52, (float) 0.37, (float) 0.47,
//                (float) 0.51};
//        b.doSort(arr, 7);
//
//        for (float i : arr) {
//            System.out.print(i + "  ");
//        }
        new StandardBucketSort().run();
    }

    @Override
    public void doSort(int[] array, int length) {
        if (length <= 0) {
            return;
        }
        ArrayList<ArrayList<Integer>> bucket = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            bucket.add(new ArrayList<>());
        }
        for (int i = 0; i < length; i++) {
            int bucketIndex = array[i] * length;
            bucket.get(bucketIndex).add(array[i]);
        }
        // Sort the elements of each bucket
        for (ArrayList<Integer> buck : bucket) {
            Collections.sort(buck);
        }
        // Get the sorted array
        int index = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0, size = bucket.get(i).size(); j < size; j++) {
                array[index++] = bucket.get(i).get(j);
            }
        }
    }

    public void doSort(float[] array, int length) {
        if (length <= 0) {
            return;
        }
        @SuppressWarnings("unchecked")
        ArrayList<Float>[] bucket = new ArrayList[length];

        // Create empty buckets
        for (int i = 0; i < length; i++) {
            bucket[i] = new ArrayList<>();
        }

        // Add elements into the buckets
        for (int i = 0; i < length; i++) {
            int bucketIndex = (int) array[i] * length;
            bucket[bucketIndex].add(array[i]);
        }

        // Sort the elements of each bucket
        for (int i = 0; i < length; i++) {
            Collections.sort((bucket[i]));
        }

        // Get the sorted array
        int index = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0, size = bucket[i].size(); j < size; j++) {
                array[index++] = bucket[i].get(j);
            }
        }
    }

}
