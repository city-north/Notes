//给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重
//复的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [-1,0,1,2,-1,-4]
//输出：[[-1,-1,2],[-1,0,1]]
// 
//
// 示例 2： 
//
// 
//输入：nums = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：nums = [0]
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 3000 
// -105 <= nums[i] <= 105 
// 
// Related Topics 数组 双指针 
// 👍 2975 👎 0


package leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 15：三数之和
 *
 * @author EricChen
 */
public class P15ThreeSum {
    public static void main(String[] args) {
        new P15ThreeSum().new Solution();
        // TO TEST
//        List<List<Integer>> ans = new ArrayList<>();
//        int len = nums.length;
//        if (len < 3) {
//            return ans;
//        }
//        Arrays.sort(nums); // 排序
//        for (int i = 0; i < len - 2; i++) {
//            if (nums[i] > 0) {
//                // 如果当前数字大于0，则三数之和一定大于0，所以结束循环
//                break;
//            }
//            if (i > 0 && nums[i] == nums[i - 1]) {
//                continue; // 去重
//            }
//            int L = i + 1;
//            int R = len - 1;
//            while (L < R) {
//                int sum = nums[i] + nums[L] + nums[R];
//                if (sum == 0) {
//                    ans.add(Arrays.asList(nums[i], nums[L], nums[R]));
//                    while (L < R && nums[L] == nums[L + 1]) {
//                        L++; // 去重
//                    }
//                    while (L < R && nums[R] == nums[R - 1]) {
//                        R--; // 去重
//                    }
//                    L++;
//                    R--;
//                } else if (sum < 0) {
//                    L++;
//                } else {
//                    R--;
//                }
//            }
//        }
//        return ans;
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            int n = nums.length;
            if (n < 3) {
                return Collections.emptyList();
            }
            List<List<Integer>> ans = new ArrayList<>();
            Arrays.sort(nums);
            for (int i = 0; i < n - 2; i++) {
                if (nums[i] > 0) {
                    continue;
                }
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }
                int L = i + 1;
                int R = n - 1;
                while (L < R) {
                    int sum = nums[i] + nums[L] + nums[R];
                    if (sum == 0) {
                        ans.add(Arrays.asList(nums[i], nums[L], nums[R]));
                        while (L < R && nums[L] == nums[L + 1]) {
                            L++;
                        }
                        while (L < R && nums[R] == nums[R - 1]) {
                            R--;
                        }
                        L++;
                        R--;
                    } else if (sum < 0) {
                        L++;
                    } else {
                        R--;
                    }
                }
            }
            return ans;
        }

//leetcode submit region end(Prohibit modification and deletion)

    }
}