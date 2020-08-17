package leetcode.editor.cn;
//给定一个二叉树，返回它的中序 遍历。
//
// 示例: 
//
// 输入: [1,null,2,3]
//   1
//    \
//     2
//    /
//   3
//
//输出: [1,3,2] 
//
// 进阶: 递归算法很简单，你可以通过迭代算法完成吗？ 
// Related Topics 栈 树 哈希表 
// 👍 629 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode(int x) { val = x; }
 * }
 */
class Solution94 {


    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<Integer>();
        Stack<Object> stack = new Stack<Object>();
        stack.add(root);
        while (!stack.empty()) {
            Object e = stack.pop();
            if (e == null) {
                continue;
            }
            if (e instanceof TreeNode) {
                // 前序遍历（根 、左、右），要做 逆序 入栈
//                stack.add(((TreeNode) e).right);
//                stack.add(((TreeNode) e).left);
//                stack.add(((TreeNode) e).val);
            } else if (e instanceof Integer) {
                list.add((Integer) e);
            }
        }
        return list;
    }


}