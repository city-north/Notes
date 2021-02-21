package algorithms.sort.base;

/**
 * <p>
 * A runnable sort algorithms
 * </p>
 *
 * @author EricChen 2021/02/20 09:23
 */
public interface RunnableSort extends SortAlgorithm, ArraySwapSupport {

    /**
     * 跑
     */
    default void run() {
        int[] array = {1, 8, 4, 2, 9, 6, 5, 7, 3};
        final long start = System.currentTimeMillis();
        doSort(array, array.length);
        final long end = System.currentTimeMillis();
        System.out.println("-----排序时间:" + (end - start) + "-----");
        for (int i : array) {
            System.out.println(i);
        }
    }


}
