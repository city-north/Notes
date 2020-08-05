package cn.eccto.study.java.leetcode;

/**
 * <p>
 * 验证是否是回文串
 * </p>
 *
 * @author EricChen 2020/08/04 21:11
 */
public class Solution125 {
    public boolean isPalindrome(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0, j = chars.length - 1; i < j; ) {
            if (!Character.isLetterOrDigit(chars[i])) {
                i++;
            } else if (!Character.isLetterOrDigit(chars[j])) {
                j--;
            } else if (Character.toLowerCase(chars[i++]) != Character.toLowerCase(chars[j--])) {
                return false;
            }
        }
        return true;
    }


}
