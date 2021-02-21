package algorithms.sort.base;

/**
 * <p>
 * SortAlgorithm
 * </p>
 *
 * @author EricChen
 */
public interface SortAlgorithm {
    /**
     * 进行排序
     *
     * @param array  原数组
     * @param length 长度
     */
    void doSort(int[] array, int length);
}
