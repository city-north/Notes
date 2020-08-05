package cn.eccto.study.java.leetcode;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/08/04 12:22
 */
public class Solution283 {

    public void moveZeroes(int[] nums) {
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[j] = nums[i];
                j++;
            }
            if (i != j) {
                nums[i] = 0;
            }
        }
    }
}
