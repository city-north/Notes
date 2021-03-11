//给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。 
//
// 说明：本题中，我们将空字符串定义为有效的回文串。 
//
// 示例 1: 
//
// 输入: "A man, a plan, a canal: Panama"
//输出: true
// 
//
// 示例 2: 
//
// 输入: "race a car"
//输出: false
// 
// Related Topics 双指针 字符串 
// 👍 347 👎 0


package leetcode.editor.cn;

/**
 * 125：验证回文串
 *
 * @author EricChen
 */
public class P125ValidPalindrome {
    public static void main(String[] args) {
        Solution solution = new P125ValidPalindrome().new Solution();
        // TO TEST
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isPalindrome(String s) {
            char[] chs = s.toCharArray();
            int i = 0, j = chs.length - 1;
            while (i < j) {
                if (!Character.isLetterOrDigit(chs[i])) {
                    i++;
                } else if (!Character.isLetterOrDigit(chs[j])) {
                    j--;
                } else if (Character.toLowerCase(chs[i++]) != Character.toLowerCase(chs[j--])) {
                    return false;
                }
            }
            return true;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}