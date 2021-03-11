//给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。 
//
// 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。你可以假设 nums1 的空间大小等于 m + n，这样它就有足够的空间保存来自 nu
//ms2 的元素。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
//输出：[1,2,2,3,5,6]
// 
//
// 示例 2： 
//
// 
//输入：nums1 = [1], m = 1, nums2 = [], n = 0
//输出：[1]
// 
//
// 
//
// 提示： 
//
// 
// nums1.length == m + n 
// nums2.length == n 
// 0 <= m, n <= 200 
// 1 <= m + n <= 200 
// -109 <= nums1[i], nums2[i] <= 109 
// 
// Related Topics 数组 双指针 
// 👍 774 👎 0


package leetcode.editor.cn;

/**
 * 88：合并两个有序数组
 *
 * @author EricChen
 */
public class P88MergeSortedArray {
    public static void main(String[] args) {
        Solution solution = new P88MergeSortedArray().new Solution();
        // TO TEST [1,2,3,0,0,0]
    }

    //    输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
//    输出：[1,2,2,3,5,6]
//    输入：nums1 = [1], m = 1, nums2 = [], n = 0
//    输出：[1]
    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            int a = m - 1;
            int b = n - 1;
            int[] t = new int[m + n];
            int k = m + n - 1;
            while (a >= 0 || b >= 0) {
                if (a < 0) {
                    t[k--] = nums2[b--];
                } else if (b < 0) {
                    t[k--] = nums1[a--];
                } else {
                    t[k--] = nums1[a] > nums2[b] ? nums1[a--] : nums2[b--];
                }
            }
            System.arraycopy(t, 0, nums1, 0, t.length);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}