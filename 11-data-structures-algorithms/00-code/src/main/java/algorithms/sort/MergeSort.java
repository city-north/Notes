package algorithms.sort;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/02/04 14:53
 */
public class MergeSort {

    public static void main(String[] args) {
        doMergeSort()
    }

    private static void doMergeSort(int[] a, int n) {
        if (n < 2) {
            returnarr;
        }
        varmiddle = Math.floor(n / 2),
                left = arr.slice(0, middle),
                right = arr.slice(middle);
        return merge(mergeSort(left), mergeSort(right));
    }


}

    function merge(left, right) {
        varresult = [];

        while (left.length > 0 && right.length > 0) {
            if (left[0] <= right[0]) {
                result.push(left.shift());
            } else {
                result.push(right.shift());
            }
        }

        while (left.length)
            result.push(left.shift());

        while (right.length)
            result.push(right.shift());

        returnresult;
    }
}
