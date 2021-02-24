//反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。 
//
// 说明: 
//1 ≤ m ≤ n ≤ 链表长度。 
//
// 示例: 
//
// 输入: 1->2->3->4->5->NULL, m = 2, n = 4
//输出: 1->4->3->2->5->NULL 
// Related Topics 链表 
// 👍 681 👎 0


package leetcode.editor.cn;

/**
 * 92：反转链表 II
 *
 * @author EricChen
 */
public class P92ReverseLinkedListIi {
    public static void main(String[] args) {
        Solution solution = new P92ReverseLinkedListIi().new Solution();
        // TO TEST
    }
    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }             输入: 1->2->3->4->5->NULL, m = 2, n = 4
     * 输出: 1->4->3->2->5->NULL
     * 1      -> 2     ->3      ->4        ->5         ->NULL
     * prevM  m, n,    postN
     */
    class Solution {
        public ListNode reverseBetween(ListNode head, int m, int n) {
            if (head == null || m >= n) {
                return head;
            }
            ListNode dummy = new ListNode(-1);
            dummy.next = head;
            ListNode prevM = dummy;
            for (int i = 1; i < m; i++) {
                prevM = prevM.next;
            }
            ListNode mNode = prevM.next;
            ListNode nNode = mNode;
            ListNode postN = nNode.next;
            for (int i = m; i < n; i++) {
                ListNode next = postN.next;
                postN.next = nNode;
                nNode = postN;
                postN = next;
            }
            prevM.next = nNode;
            mNode.next = postN;
            return dummy.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

//    public ListNode reverseBetween(ListNode head, int m, int n) {
//        if (head == null || m >= n) {
//            return head;
//        }
//        ListNode dummy = new ListNode(-1);
//        dummy.next = head;
//        ListNode prevM = dummy;
//        for (int i = 1; i < m; i++) {
//            prevM = prevM.next;
//        }
//        ListNode mNode = prevM.next;
//        ListNode nNode = mNode;
//        ListNode postN = nNode.next;
//        for (int i = m; i < n; i++) {
//            ListNode next = postN.next;
//            postN.next = nNode;
//            nNode = postN;
//            postN = next;
//        }
//        mNode.next = postN;
//        prevM.next = nNode;
//        return dummy.next;
//    }
}