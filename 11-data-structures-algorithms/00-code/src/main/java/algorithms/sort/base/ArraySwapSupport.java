package algorithms.sort.base;

/**
 * <p>
 * ArraySwapSupport
 * </p>
 *
 * @author EricChen
 */
public interface ArraySwapSupport {

    /**
     * 交换两个位置
     *
     * @param array 数组
     * @param left  左边
     * @param right 右边
     */
    default void swap(int[] array, int left, int right) {
        int temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }
}
